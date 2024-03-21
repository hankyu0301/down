package com.example.demo.global.auth.guard;

import com.example.demo.domain.user.entity.EnumUserRole;
import com.example.demo.domain.util.AuthUtil;

import java.util.List;

public abstract class Guard {
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    abstract protected List<EnumUserRole> getRoleTypes();
    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<EnumUserRole> roleTypes) {
        return AuthUtil.extractEnumUserRole().containsAll(roleTypes);
    }
}
