package net.jaggerwang.scip.gateway.adapter.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.async.FileAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.async.PostAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.async.StatAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.async.UserAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


abstract public class AbstractDataFetcher {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserAsyncService userAsyncService;

    @Autowired
    protected PostAsyncService postAsyncService;

    @Autowired
    protected FileAsyncService fileAsyncService;

    @Autowired
    protected StatAsyncService statAsyncService;

    public Map<String, DataFetcher> toMap() {
        var dataFetchers = new HashMap<String, DataFetcher>();
        var methods = this.getClass().getDeclaredMethods();
        for (var method: methods) {
            if (Modifier.isPublic(method.getModifiers()) &&
                    method.getReturnType().equals(DataFetcher.class)) {
                try {
                    dataFetchers.put(method.getName(), (DataFetcher) method.invoke(this));
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataFetchers;
    }

    protected <T> CompletableFuture<T> monoWithContext(Mono<T> mono, DataFetchingEnvironment env) {
        return mono
                .subscriberContext(ctx -> env.getContext())
                .toFuture();
    }
}
