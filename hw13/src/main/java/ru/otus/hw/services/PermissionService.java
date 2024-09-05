package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PermissionService {

    private final MutableAclService mutableAclService;

    public void addPermissionForAuthority(Permission permission, ObjectIdentity objectIdentity, Sid owner, Sid authority) {

        MutableAcl acl = mutableAclService.createAcl(objectIdentity);
        acl.setOwner(owner);
        acl.insertAce(acl.getEntries().size(), permission, authority, true);
        mutableAclService.updateAcl(acl);
    }
}
