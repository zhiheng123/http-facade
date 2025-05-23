/*
 * Copyright 2024 OpenFacade Authors
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

package io.github.openfacade.http.tests;

import io.github.openfacade.http.HttpServerConfig;
import io.github.openfacade.http.ReactorHttpClientConfig;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ReactorBaseTest extends BaseTest {
    protected ReactorHttpClientConfig reactorHttpClientConfig() {
        ReactorHttpClientConfig reactorClientConfig = new ReactorHttpClientConfig.Builder().build();
        return reactorClientConfig;
    }

    protected Stream<Arguments> reactorClientServerConfigProvider() {
        List<HttpServerConfig> httpServerConfigs = serverConfigList();
        ReactorHttpClientConfig reactorClientConfig = reactorHttpClientConfig();
        return httpServerConfigs.stream().map(serverConfig -> Arguments.arguments(reactorClientConfig, serverConfig));
    }
}
