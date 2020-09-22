package com.github.wujiuye.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.config.PackageConfig;

/**
 * @author wujiuye 2020/09/23
 */
public class EasyGeneratorTest {

    public static void main(String[] args) throws Exception {
        PackageConfig config = new PackageConfig()
                .setParent("com.github.wujiuye.generator")
                .setEntity("repo.dao.po")
                .setMapper("repo.dao")
                .setXml("repo.dao");
        EasyMybatisGenerator.run(config, "pay_config_rec");
    }

}
