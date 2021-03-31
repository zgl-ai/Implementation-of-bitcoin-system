package consensus;

import data.*;
import utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 生成随机交易
 */
public class TransactionProducer extends Thread {

    private TransactionPool transactionPool;
    private final BlockChain blockChain;

    public TransactionProducer(TransactionPool transactionPool,BlockChain blockChain) {
        this.transactionPool = transactionPool;
        this.blockChain=blockChain;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (transactionPool) {
                while (transactionPool.isFull()) {
                    try {
                        transactionPool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Transaction randomOne = getOneTransaction();
                transactionPool.put(randomOne);
                if (transactionPool.isFull()) {
                    transactionPool.notify();
                }
            }
        }
    }

    //随机生成一笔交易
    private Transaction getOneTransaction() {
        Random random =new Random();
        Transaction transaction=null;
        Account[] accounts =blockChain.getAccounts();//获得用户数组

        while(true){
            Account aAccount=accounts[random.nextInt(accounts.length)];//随机生成一个a用户
            Account bAccount=accounts[random.nextInt(accounts.length)];//随机生成一个b用户

            //a用户和b用户不能相等
            if(aAccount==bAccount){
                continue;
            }
            //获得a，b用户的钱包地址
            String aWalletAddress=aAccount.getWalletAddress();
            String bWalletAddress=aAccount.getWalletAddress();

            //统计出转入地址为a的utxo
            UTXO[] aTrueUtxos=blockChain.getTrueUtxos(aWalletAddress);
            int aAmount=aAccount.getAmount(aTrueUtxos);
            //a余额为零就重新生成
            if(aAmount==0){
                continue;
            }

            //随机生成交易数额
            int txAmount =random.nextInt(aAmount)+1;

            List<UTXO> inUtxoList=new ArrayList<>();
            List<UTXO> outUtxoList=new ArrayList<>();

            //a生成自己的签名
            byte[] aUnlockSign = SecurityUtil.signature(aAccount.getPublicKey().getEncoded(),aAccount.getPrivateKey());

            int inAmount=0;
            //选择那些大于交易总额的utxo，记入inUtxoList
            for(UTXO utxo :aTrueUtxos){
                //a要解锁自己的脚本才能使用utxo
                if(utxo.unlockScript(aUnlockSign,aAccount.getPublicKey())){
                    inAmount+=utxo.getAmount();
                    inUtxoList.add(utxo);
                    if(inAmount>=txAmount){
                        break;//直到能够大于交易总额
                    }
                }
            }

            //？
            if(inAmount<txAmount){
                continue;
            }
            //写下一笔记入b的utxo
            outUtxoList.add(new UTXO(bWalletAddress,txAmount,bAccount.getPublicKey()));

            //把多余的钱转回a的钱包中，记入一笔utxo
            if(inAmount>txAmount){
                outUtxoList.add(new UTXO(aWalletAddress,inAmount-txAmount,aAccount.getPublicKey()));

            }
            UTXO[] inUtxos=inUtxoList.toArray(new UTXO[0]);
            UTXO[] outUtxos=outUtxoList.toArray(new UTXO[0]);

            //a通过交易的utxo生成sign，然后记入transaction
            byte[] data =SecurityUtil.utxos2Bytes(inUtxos,outUtxos);
            byte[] sign=SecurityUtil.signature(data,aAccount.getPrivateKey());
            long timestamp=System.currentTimeMillis();
            transaction=new Transaction(inUtxos,outUtxos,sign,aAccount.getPublicKey(),timestamp);
            break;
        }
        return transaction;
    }

}
