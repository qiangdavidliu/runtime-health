/**
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.runtime.health.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Static builder of for creating an {@link IndicatorMatcher} instance. Excluded 
 * indicators will take priority over included ones. 
 * 
 * <pre>
 *  IndicatorMatchers.includes(someIndicatorInstance)
 *                  .excludes("someIndicatorName")
 *                  .build();
 * </pre>
 */
public class IndicatorMatchers {
    
    public static IndicatorMatcherBuilder includes(String... indicatorNames) {
        return new IndicatorMatcherBuilder().includes(indicatorNames);
    }
    
    public static IndicatorMatcherBuilder excludes(String... indicatorNames) {
        return new IndicatorMatcherBuilder().excludes(indicatorNames);
    }
    
    public static IndicatorMatcher build() {
        return unused -> true;
    }

    public static class IndicatorMatcherBuilder {
        
        public final List<String> includedIndicatorNames;
        public final List<String> excludedIndicatorNames;
        
        public IndicatorMatcherBuilder() {
            this.includedIndicatorNames = new ArrayList<>();
            this.excludedIndicatorNames = new ArrayList<>();
        }
        
        public IndicatorMatcherBuilder includes(String... indicatorNames) {
            if (indicatorNames != null) {
                includedIndicatorNames.addAll(Arrays.asList(indicatorNames));
            }
            return this;
        }
        
        public IndicatorMatcherBuilder excludes(String... indicatorNames) {
            if (indicatorNames != null) {
                excludedIndicatorNames.addAll(Arrays.asList(indicatorNames));
            }
            return this;
        }
        
        public IndicatorMatcher build() {
            return indicator -> {
                return excludedIndicatorNames.stream().noneMatch(i -> indicator.getName().equals(i))
                        && (includedIndicatorNames.isEmpty()
                                || includedIndicatorNames.stream().anyMatch(indicator.getName()::equals));
            };
        }
        
    }
}

