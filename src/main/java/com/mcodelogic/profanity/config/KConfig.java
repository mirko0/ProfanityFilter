package com.mcodelogic.profanity.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import lombok.Getter;

@Getter
public class KConfig {

    public static final BuilderCodec<KConfig> CODEC = BuilderCodec.builder(KConfig.class, KConfig::new)

            .append(new KeyedCodec<String>("BypassPermission", Codec.STRING),
                    (config, value, extra) -> config.BypassPermission = value,
                    (config, extra) -> config.BypassPermission)
            .add()

            .append(new KeyedCodec<String>("FilterMode", Codec.STRING),
                    (config, value, extra) -> config.FilterMode = value,
                    (config, extra) -> config.FilterMode)
            .add()

            .build();

    private String BypassPermission = "profanityfilter.bypass";
    private String FilterMode = "MODERATE";

    public KConfig() {
    }

}