/**
 * Copyright (C) 2010 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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

package com.github.rvesse.airline;

import com.github.rvesse.airline.builder.ParserBuilder;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.command.SingleCommandParser;
import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class for encapsulating single commands
 *
 * @param <C>
 *            Command type
 */
public class SingleCommand<C> {
    /**
     * Creates a new single command
     * 
     * @param command
     *            Command class
     * @return Single command parser
     */
    public static <C> SingleCommand<C> singleCommand(Class<C> command) {
        return new SingleCommand<C>(command, null);
    }

    /**
     * Creates a new single command
     * 
     * @param command
     *            Command class
     * @param parserConfig
     *            Parser configuration to use, if {@code null} the default
     *            configuration is used
     * @return Single command parser
     */
    public static <C> SingleCommand<C> singleCommand(Class<C> command, ParserMetadata<C> parserConfig) {
        return new SingleCommand<C>(command, parserConfig);
    }

    private final ParserMetadata<C> parserConfig;
    private final CommandMetadata commandMetadata;

    private SingleCommand(Class<C> command, ParserMetadata<C> parserConfig) {
        checkNotNull(command, "command is null");
        this.parserConfig = parserConfig != null ? parserConfig : ParserBuilder.<C> defaultConfiguration();

        commandMetadata = MetadataLoader.loadCommand(command);
    }

    /**
     * Gets the command metadata
     * 
     * @return Command metadata
     */
    public CommandMetadata getCommandMetadata() {
        return commandMetadata;
    }

    /**
     * Gets the parser configuration
     * 
     * @return Parser configuration
     */
    public ParserMetadata<C> getParserConfiguration() {
        return parserConfig;
    }

    public C parse(String... args) {
        return parse(ImmutableList.copyOf(args));
    }

    public C parse(Iterable<String> args) {
        SingleCommandParser<C> parser = new SingleCommandParser<C>();
        return parser.parse(parserConfig, commandMetadata, args);
    }
}
