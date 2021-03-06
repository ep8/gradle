/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.execution.steps;

import org.gradle.internal.execution.CacheHandler;
import org.gradle.internal.execution.CachingContext;
import org.gradle.internal.execution.IncrementalChangesContext;
import org.gradle.internal.execution.Result;
import org.gradle.internal.execution.Step;
import org.gradle.internal.execution.UnitOfWork;
import org.gradle.internal.execution.history.AfterPreviousExecutionState;
import org.gradle.internal.execution.history.BeforeExecutionState;
import org.gradle.internal.execution.history.changes.ExecutionStateChanges;

import java.util.Optional;

public class PrepareCachingStep<C extends IncrementalChangesContext, R extends Result> implements Step<C, R> {
    private final Step<? super CachingContext, ? extends R> delegate;

    public PrepareCachingStep(Step<? super CachingContext, ? extends R> delegate) {
        this.delegate = delegate;
    }

    @Override
    public R execute(C context) {
        CacheHandler cacheHandler = context.getWork().createCacheHandler();
        return delegate.execute(new CachingContext() {
            @Override
            public CacheHandler getCacheHandler() {
                return cacheHandler;
            }

            @Override
            public Optional<ExecutionStateChanges> getChanges() {
                return context.getChanges();
            }

            @Override
            public Optional<String> getRebuildReason() {
                return context.getRebuildReason();
            }

            @Override
            public Optional<AfterPreviousExecutionState> getAfterPreviousExecutionState() {
                return context.getAfterPreviousExecutionState();
            }

            @Override
            public Optional<BeforeExecutionState> getBeforeExecutionState() {
                return context.getBeforeExecutionState();
            }

            @Override
            public UnitOfWork getWork() {
                return context.getWork();
            }
        });
    }
}
