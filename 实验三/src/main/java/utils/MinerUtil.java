package utils;

import config.MiniChainConfig;

public class MinerUtil {

    /**
     * 静态方法，使用MinerUtil.hashPrefixTarget()即可调用
     * 该方法根据根据挖矿的难度值设置，返回一个相应数量的0组成的字符串，供挖矿线程判断其区块是否满足难度条件
     *
     * @return 根据挖矿难度值，相应数量的0组成的字符串
     */
    public static String hashPrefixTarget() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < MiniChainConfig.DIFFICULTY; i++) {
            stringBuilder.append("0");
        }
        return stringBuilder.toString();
    }

}
