package com.github.rvesse.airline.parser.command;

import static com.github.rvesse.airline.parser.ParserUtil.createInstance;

import java.util.HashMap;
import java.util.Map;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.AbstractCommandParser;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.GlobalRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;

public class CliParser<T> extends AbstractCommandParser<T> {

    public T parse(GlobalMetadata<T> metadata, Iterable<String> args) {
        if (args == null)
            throw new NullPointerException("args cannot be null");

        ParseState<T> state = tryParse(metadata, args);

        // If we did not find a command choose the appropriate default command
        // (if any)
        if (state.getCommand() == null) {
            if (state.getGroup() != null) {
                state = state.withCommand(state.getGroup().getDefaultCommand());
            } else {
                state = state.withCommand(metadata.getDefaultCommand());
            }
        }

        validate(state);

        CommandMetadata command = state.getCommand();

        Map<Class<?>, Object> bindings = new HashMap<Class<?>, Object>();
        bindings.put(GlobalMetadata.class, metadata);

        if (state.getGroup() != null) {
            bindings.put(CommandGroupMetadata.class, state.getGroup());
        }

        if (state.getCommand() != null) {
            bindings.put(CommandMetadata.class, state.getCommand());
        }

        bindings.put(ParserMetadata.class, state.getParserConfiguration());

        //@formatter:off
        return createInstance(command.getType(), 
                              command.getAllOptions(), 
                              state.getParsedOptions(),
                              command.getArguments(), 
                              state.getParsedArguments(), 
                              command.getMetadataInjections(), 
                              AirlineUtils.unmodifiableMapCopy(bindings),
                              state.getParserConfiguration().getCommandFactory());
        //@formatter:on
    }

    /**
     * Validates the parser state
     * <p>
     * This includes things like verifying we ended in an appropriate state,
     * that all required options and arguments were present etc
     * </p>
     * 
     * @param state
     *            Parser state
     */
    protected void validate(ParseState<T> state) {
        // Global restrictions
        for (GlobalRestriction restriction : state.getGlobal().getRestrictions()) {
            restriction.validate(state);
        }
        CommandMetadata command = state.getCommand();

        // Argument restrictions
        ArgumentsMetadata arguments = command.getArguments();
        if (arguments != null) {
            for (ArgumentsRestriction restriction : arguments.getRestrictions()) {
                restriction.postValidate(state, arguments);
            }
        }

        // Option restrictions
        for (OptionMetadata option : command.getAllOptions()) {
            for (OptionRestriction restriction : option.getRestrictions()) {
                restriction.postValidate(state, option);
            }
        }
    }
}
