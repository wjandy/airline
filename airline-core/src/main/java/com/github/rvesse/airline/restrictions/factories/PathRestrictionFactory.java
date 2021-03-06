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
package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.annotations.restrictions.Directory;
import com.github.rvesse.airline.annotations.restrictions.File;
import com.github.rvesse.airline.annotations.restrictions.Path;
import com.github.rvesse.airline.annotations.restrictions.PathKind;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.PathRestriction;

public class PathRestrictionFactory implements OptionRestrictionFactory, ArgumentsRestrictionFactory {

    protected final PathRestriction createCommon(Annotation annotation) {
        if (annotation instanceof Path) {
            Path path = (Path) annotation;
            return new PathRestriction(path.mustExist(), path.readable(), path.writable(), path.executable(),
                    path.kind());
        } else if (annotation instanceof File) {
            File path = (File) annotation;
            return new PathRestriction(path.mustExist(), path.readable(), path.writable(), path.executable(),
                    PathKind.FILE);
        } else if (annotation instanceof Directory) {
            Directory path = (Directory) annotation;
            return new PathRestriction(path.mustExist(), path.readable(), path.writable(), path.executable(),
                    PathKind.DIRECTORY);
        }
        return null;
    }

    @Override
    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        return createCommon(annotation);
    }

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        return createCommon(annotation);
    }
    
    protected List<Class<? extends Annotation>> supportedAnnotations() {
        ArrayList<Class<? extends Annotation>> supported = new ArrayList<>();
        supported.add(Path.class);
        supported.add(File.class);
        supported.add(Directory.class);
        return supported;
    }

    @Override
    public List<Class<? extends Annotation>> supportedArgumentsAnnotations() {
        return supportedAnnotations();
    }

    @Override
    public List<Class<? extends Annotation>> supportedOptionAnnotations() {
        return supportedAnnotations();
    }

}
