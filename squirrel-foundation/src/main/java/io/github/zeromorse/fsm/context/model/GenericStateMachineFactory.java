package io.github.zeromorse.fsm.context.model;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.CoreErrorCodes;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.FsmAgentRuntimeException;
import com.sankuai.meituan.waimai.config.fsm.agent.util.SPIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrelframework.foundation.component.IdProvider;
import org.squirrelframework.foundation.component.SquirrelProvider;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineConfiguration;
import org.squirrelframework.foundation.fsm.StateMachineImporter;
import org.squirrelframework.foundation.util.TypeReference;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class GenericStateMachineFactory {
    private static final Logger logger = LoggerFactory.getLogger(GenericStateMachineFactory.class);

    private static StateMachineImporter<GenericStateMachine, String, String, GenericContext> importer;
    private static LoadingCache<String, StateMachineBuilder<GenericStateMachine, String, String, GenericContext>> builderCache;

    static {
        importer = SquirrelProvider.getInstance().newInstance(new TypeReference<StateMachineImporter<GenericStateMachine, String, String, GenericContext>>() {
        });

        builderCache = CacheBuilder.newBuilder().build(new CacheLoader<String, StateMachineBuilder<GenericStateMachine, String, String, GenericContext>>() {
            @Override
            public StateMachineBuilder<GenericStateMachine, String, String, GenericContext> load(String key) throws Exception {
                logger.debug("load statemachine builder for {} start...", key);
                return importer.importFromString(key);
            }
        });
    }

    public static GenericStateMachine create(String sqrlScxml, final String fsmId, @Nullable String initialState) {
        StateMachineBuilder<GenericStateMachine, String, String, GenericContext> builder = getBuilder(sqrlScxml);
        StateMachineConfiguration configuration = StateMachineConfiguration.create().setIdProvider(new IdProvider() {
            @Override
            public String get() {
                return fsmId;
            }
        });
        if (Strings.isNullOrEmpty(initialState)) {
            initialState = null;
        }
        GenericStateMachine stateMachine = builder.newStateMachine(initialState, configuration);
        enhance(stateMachine);
        return stateMachine;
    }

    /**
     * 增强状态机实例
     */
    private static void enhance(GenericStateMachine stateMachine) {
        Iterator<GenericStateMachineEnhancer> iterator = SPIUtil.load(GenericStateMachineEnhancer.class);
        while (iterator.hasNext()) {
            iterator.next().enhance(stateMachine);
        }
    }

    private static StateMachineBuilder<GenericStateMachine, String, String, GenericContext> getBuilder(String sqrlScxml) {
        try {
            return builderCache.get(sqrlScxml);
        } catch (ExecutionException e) {
            logger.error("create GenericStateMachine failure, sqrlScxml = {}", sqrlScxml, e);
            throw new FsmAgentRuntimeException(e.getCause(), CoreErrorCodes.BUILD_FSM_FAILURE, sqrlScxml);
        }
    }
}
