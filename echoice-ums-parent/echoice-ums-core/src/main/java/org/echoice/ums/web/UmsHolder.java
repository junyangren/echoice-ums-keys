package org.echoice.ums.web;

import org.echoice.ums.domain.EcGroup;

public class UmsHolder {
	
    /**
     * ThreadLocal to hold the Assertion for Threads to access.
     */
    private static final ThreadLocal<UmsAppBean> threadLocal = new ThreadLocal<UmsAppBean>();
    
    public static String getUserAlias() {
        return threadLocal.get().getUserAlias();
    }

    /**
     * 获取用户组
     * @return
     */
    public static EcGroup getUserGroup() {
        return threadLocal.get().getEcGroup();
    }
    
    /**
     * 是否用部门管理员
     * @return
     */
    public static boolean isAdmin() {
        return threadLocal.get().isAdmin();
    }
    
    /**
     * Retrieve the assertion from the ThreadLocal.
     *
     * @return the Asssertion associated with this thread.
     */
    public static UmsAppBean getUmsAppBean() {
        return threadLocal.get();
    }

    /**
     * Add the Assertion to the ThreadLocal.
     *
     * @param assertion the assertion to add.
     */
    public static void setUmsAppBean(final UmsAppBean umsAppBean) {
        threadLocal.set(umsAppBean);
    }

    /**
     * Clear the ThreadLocal.
     */
    public static void clear() {
        threadLocal.set(null);
    }
}
