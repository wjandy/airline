/**
 * Copyright (C) 2010-16 the original author or authors.
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

package com.github.rvesse.airline.prompts.formatters;

import com.github.rvesse.airline.io.printers.UsagePrinter;
import com.github.rvesse.airline.prompts.Prompt;

public class ListFormat<TOption> implements PromptFormatter {
    
    private final boolean withNumbering;
    private final int columns;
    
    public ListFormat() {
        this(true, 80);
    }
    
    public ListFormat(boolean showOptionNumbers, int columns) {
        this.withNumbering = showOptionNumbers;
        this.columns = columns;
    }

    @Override
    public <T> void displayPrompt(Prompt<T> prompt) {
        UsagePrinter printer = new UsagePrinter(prompt.getProvider().getPromptStream(), this.columns);
        printer.append(String.format("%s: ", prompt.getMessage()));
        printer.flush();
        
        UsagePrinter optionPrinter = printer.newIndentedPrinter(2);
        
        int index = 1;
        for (T option : prompt.getOptions()) {
            if (this.withNumbering) {
                optionPrinter.append(String.format("- %d) %s", index, option.toString()));
                index++;
            } else {
                optionPrinter.append(String.format("- %s", option.toString()));
            }
            optionPrinter.newline();
        }
        optionPrinter.flush();
        printer.flush();
        
    }

}