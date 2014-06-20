package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.Pagination;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.core.validator.ValidatorStrategy;
import com.edgar.module.sys.repository.domain.SysRoleRoute;
import com.edgar.module.sys.repository.domain.SysRoute;
import com.edgar.module.sys.repository.domain.SysRouteRes;
import com.edgar.module.sys.validator.SysRouteUpdateValidator;
import com.edgar.module.sys.validator.SysRouteValidator;

/**
 * 路由的业务逻辑实现类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
@Service
public class SysRouteServiceImpl implements SysRouteService {
        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoute> sysRouteDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRouteRes> sysRouteResDao;

        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoleRoute> sysRoleRouteDao;

        private ValidatorStrategy validator = new SysRouteValidator();

        private ValidatorStrategy updateValidator = new SysRouteUpdateValidator();

        @Override
        public List<SysRoute> findAll() {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("isRoot", 0);
                return sysRouteDao.query(example);
        }

        @Override
        @Transactional
        public int save(SysRoute sysRoute) {
                Assert.notNull(sysRoute);
                validator.validator(sysRoute);
                sysRoute.setIsRoot(false);
                sysRoute.setRouteId(IDUtils.getNextId());
                return sysRouteDao.insert(sysRoute);
        }

        @Override
        @Transactional
        public int update(SysRoute sysRoute) {
                updateValidator.validator(sysRoute);
                Assert.notNull(sysRoute);
                return sysRouteDao.update(sysRoute);
        }

        @Override
        public SysRoute get(int routeId) {
                return sysRouteDao.get(routeId);
        }

        @Override
        public Pagination<SysRoute> pagination(QueryExample example, int page, int pageSize) {
                example.equalsTo("isRoot", 0);
                return sysRouteDao.pagination(example, page, pageSize);
        }

        @Override
        @Transactional
        public int deleteWithLock(int routeId, long updatedTime) {
                int result = sysRouteDao.deleteByPkAndVersion(routeId, updatedTime);
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("routeId", routeId);
                sysRoleRouteDao.delete(example);
                sysRouteResDao.delete(example);
                return result;
        }

        @Override
        public List<SysRouteRes> getResource(int routeId) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("routeId", routeId);
                return sysRouteResDao.query(example);
        }

        @Override
        @Transactional
        public void saveRouteRes(RouteResCommand command) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("routeId", command.getRouteId());
                sysRouteResDao.delete(example);
                List<SysRouteRes> sysRouteReses = new ArrayList<SysRouteRes>();
                if (command.getResourceIds() == null) {
                        return;
                }
                for (Integer resId : command.getResourceIds()) {
                        SysRouteRes sysRouteRes = new SysRouteRes();
                        sysRouteRes.setRouteResId(IDUtils.getNextId());
                        sysRouteRes.setRouteId(command.getRouteId());
                        sysRouteRes.setResourceId(resId);
                        sysRouteReses.add(sysRouteRes);
                }
                sysRouteResDao.insert(sysRouteReses);
                SysRoute sysRoute = sysRouteDao.get(command.getRouteId());
                if (sysRoute == null) {
                        throw ExceptionFactory.isNull("msg.error.route.notexists");
                }
        }

}
