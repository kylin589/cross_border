package io.renren.modules.sys.vm;

/**
 * @Auther: wdh
 * @Date: 2018/12/21 19:52
 * @Description:
 */
public class DeptMergeSeparateVM {
    private Long fromDeptId;
    private Long toDeptId;
    private Long[] userIds;

    public Long getFromDeptId() {
        return fromDeptId;
    }

    public void setFromDeptId(Long fromDeptId) {
        this.fromDeptId = fromDeptId;
    }

    public Long getToDeptId() {
        return toDeptId;
    }

    public void setToDeptId(Long toDeptId) {
        this.toDeptId = toDeptId;
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Long[] userIds) {
        this.userIds = userIds;
    }
}
