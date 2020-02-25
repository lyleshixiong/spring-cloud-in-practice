package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import org.springframework.stereotype.Component;

@Component
public class PostStatDataFetcher extends AbstractDataFetcher {
    public DataFetcher post() {
        return env -> {
            PostStatDto postStatDto = env.getSource();
            return monoWithContext(postAsyncService.info(postStatDto.getPostId()), env);
        };
    }
}
