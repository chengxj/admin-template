package com.edgar.module.sys.validator;

import java.lang.annotation.ElementType;

import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.hibernate.validator.cfg.defs.SizeDef;

import com.edgar.core.validator.AbstractValidatorTemplate;
import com.edgar.module.sys.service.PasswordCommand;

public class PasswordValidator extends AbstractValidatorTemplate {

        @Override
        public Validator createValidator() {
                HibernateValidatorConfiguration configuration = Validation.byProvider(
                                HibernateValidator.class).configure();

                ConstraintMapping constraintMapping = configuration.createConstraintMapping();

                constraintMapping.type(PasswordCommand.class)
                                .property("oldpassword", ElementType.FIELD)
                                .constraint(new SizeDef().max(16).min(6))
                                .constraint(new NotNullDef())
                                .property("newpassword", ElementType.FIELD)
                                .constraint(new SizeDef().max(16).min(6))
                                .constraint(new NotNullDef())
                                .property("retypepassword", ElementType.FIELD)
                                .constraint(new SizeDef().max(16).min(6))
                                .constraint(new NotNullDef());
                return configuration.addMapping(constraintMapping).buildValidatorFactory()
                                .getValidator();
        }

}
