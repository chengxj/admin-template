package com.edgar.module.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.Setter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edgar.core.command.CommandHandler;
import com.edgar.core.command.CommandResult;
import com.edgar.core.repository.CrudRepository;
import com.edgar.core.repository.IDUtils;
import com.edgar.core.repository.QueryExample;
import com.edgar.core.util.ExceptionFactory;
import com.edgar.module.sys.repository.domain.SysResource;
import com.edgar.module.sys.repository.domain.SysRoleResource;

@Service
public class ResourcePermissionHandler implements CommandHandler<ResourcePermission> {
        
        @Autowired
        @Setter
        private CrudRepository<Integer, SysResource> sysResourceDao;
        
        @Autowired
        @Setter
        private CrudRepository<Integer, SysRoleResource> sysRoleResourceDao;

        @Override
        @Transactional
        public CommandResult<Integer> execute(ResourcePermission command) {
                int roleId = command.getRoleId();
                int result = deleteByRole(roleId);
                Set<Integer> resourceIds = command.getResourceIds();
                if (CollectionUtils.isEmpty(resourceIds)) {
                        return CommandResult.newInstance(result);
                }
                Set<Integer> unModifiableRouteIds = Collections.unmodifiableSet(resourceIds);
                saveRoleRes(roleId, unModifiableRouteIds);

                command.setResourceIds(resourceIds);
                return CommandResult.newInstance(result);
        }
        
        /**
         * 保存资源权限
         * 
         * @param roleId
         *                角色ID
         * @param resourceIds
         *                资源的ID集合
         */
        private void saveRoleRes(int roleId, Set<Integer> resourceIds) {
                List<SysRoleResource> sysRoleResources = new ArrayList<SysRoleResource>();
                for (Integer resourceId : resourceIds) {
                        SysRoleResource sysRoleRes = new SysRoleResource();
                        sysRoleRes.setRoleId(roleId);
                        sysRoleRes.setResourceId(resourceId);
                        sysRoleRes.setRoleResourceId(IDUtils.getNextId());
                        sysRoleResources.add(sysRoleRes);
                        SysResource sysRoute = sysResourceDao.get(resourceId);
                        if (sysRoute == null) {
                                throw ExceptionFactory.isNull("msg.error.resource.notexists");
                        }
                }
                sysRoleResourceDao.insert(sysRoleResources);
        }
        
        /**
         * 根据角色删除资源权限
         * 
         * @param roleId
         *                角色ID
         * @return 返回删除的记录数
         */
        private int deleteByRole(int roleId) {
                QueryExample example = QueryExample.newInstance();
                example.equalsTo("roleId", roleId);
                return sysRoleResourceDao.delete(example);
        }

}
