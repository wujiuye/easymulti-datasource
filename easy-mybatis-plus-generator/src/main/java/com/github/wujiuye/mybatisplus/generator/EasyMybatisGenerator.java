package com.github.wujiuye.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.github.wujiuye.mybatisplus.generator.engine.OnlyMappperTemplateEngine;
import com.github.wujiuye.mybatisplus.generator.util.GeneratorConfigUtils;

/**
 * mybatis代码生成器
 *
 * @author wujiuye 2020/04/22
 */
public class EasyMybatisGenerator {

    /**
     * 开始生成代码
     *
     * @param packageConfig 包名配置信息，包名用'.'符号
     * @param tables        此处需要生成的表
     */
    public static void run(PackageConfig packageConfig, String... tables) throws Exception {
        new AutoGenerator().setTemplateEngine(new OnlyMappperTemplateEngine())
                .setGlobalConfig(GeneratorConfigUtils.getGlobalConfig())
                .setDataSource(GeneratorConfigUtils.getDataSourceConfig())
                .setStrategy(GeneratorConfigUtils.getStrategyConfig(tables))
                .setPackageInfo(packageConfig)
                .execute();
    }

}
