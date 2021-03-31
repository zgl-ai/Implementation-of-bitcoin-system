package unit;

import consensus.MinerNode;
import data.*;
import utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

public class UtxoTest {
    @org.junit.Test
    public void utxoTest(){
        //构造出一个确定的初始链
        BlockChain blockChain=new BlockChain();
        //构造出一个确定的交易池
        TransactionPool transactionPool=new TransactionPool(1);
        //构造出一个确定的矿工
        MinerNode minerNode=new MinerNode(transactionPool,blockChain);
        //通过getOneTransaction，构造出一笔确定的的交易
        Transaction transaction=getOneTransaction(blockChain);
        transactionPool.put(transaction);
        minerNode.run();
    }

    private Transaction getOneTransaction(BlockChain blockChain) {
        Transaction transaction=null;
        Account[] accounts =blockChain.getAccounts();
        Account aAccount=accounts[1];
        Account bAccount=accounts[2];

        String aWalletAddress=aAccount.getWalletAddress();
        String bWalletAddress=aAccount.getWalletAddress();

        int txAmount=1000;
        //统计出转入地址为a的utxo
        UTXO[] aTrueUtxos=blockChain.getTrueUtxos(aWalletAddress);
        int aAmount=aAccount.getAmount(aTrueUtxos);
        //a余额为零就重新生成
        if(aAmount==0){
            return null;
        }

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
            return null;
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
        return transaction;
    }
}
