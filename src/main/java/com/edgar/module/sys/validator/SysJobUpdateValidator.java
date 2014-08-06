package com.edgar.module.sys.validator;

import java.lang.annotation.ElementType;

import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.hibernate.validator.cfg.defs.NullDef;
import org.hibernate.validator.cfg.defs.SizeDef;

import com.edgar.core.validator.AbstractValidatorTemplate;
import com.edgar.module.sys.repository.domain.SysJob;

public class SysJobUpdateValidator extends AbstractValidatorTemplate {

        @Override
        public Validator createValidator() {
                HibernateValidatorConfiguration configuration = Validation.byProvider(
                                HibernateValidator.class).configure();

                ConstraintMapping constraintMapping = configuration.createConstraintMapping();

                constraintMapping.type(SysJob.class).property("jobId", ElementType.FIELD)
                                .constraint(new NotNullDef())
                                .property("clazzName", ElementType.FIELD).constraint(new NullDef())
                                .property("jobName", ElementType.FIELD)
                                .constraint(new SizeDef().max(32))
                                .property("cron", ElementType.FIELD)
                                .constraint(new SizeDef().max(32));
                return configuration.addMapping(constraintMapping).buildValidatorFactory()
                                .getValidator();
        }

}
