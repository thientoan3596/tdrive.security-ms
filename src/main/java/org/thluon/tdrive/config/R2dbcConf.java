package org.thluon.tdrive.config;

import com.github.thientoan3596.BytesToUUIDConverter;
import com.github.thientoan3596.UUIDToBytesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.List;

@Configuration
@EnableR2dbcRepositories
public class R2dbcConf {
    @Bean
    R2dbcCustomConversions r2dbcCustomConversions() {
        return new R2dbcCustomConversions(
                CustomConversions.StoreConversions.NONE,
                List.of(
                        new UUIDToBytesConverter(),
                        new BytesToUUIDConverter()
                ));
    }
}
