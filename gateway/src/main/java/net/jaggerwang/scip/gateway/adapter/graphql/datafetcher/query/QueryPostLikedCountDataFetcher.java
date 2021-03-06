package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.query;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import org.springframework.stereotype.Component;

@Component
public class QueryPostLikedCountDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        var userId = env.getArgument("userId") != null ?
                Long.valueOf((Integer) env.getArgument("userId")) : null;
        return postAsyncService.likedCount(userId);
    }
}
