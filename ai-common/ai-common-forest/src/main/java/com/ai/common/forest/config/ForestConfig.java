package com.ai.common.forest.config;

import com.dtflys.forest.springboot.annotation.ForestScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * Forest HTTP 客户端配置
 *
 * @author root 2026-05-16 16:04
 */
@Slf4j
@AutoConfiguration
@ForestScan("com.ai.agent")
public class ForestConfig {

}
