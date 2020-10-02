# IdentityService 用户和用户组管理服务

Java Doc :[https://www.activiti.org/javadocs/](https://www.activiti.org/javadocs/)

## 服务列表

| 序号 | Service名称       | 主要作用                                           |
| ---- | ----------------- | -------------------------------------------------- |
| 1    | RepositoryService | 流程部署、流程定义                                 |
| 2    | TaskService       | 对流程实例进行管理和控制                           |
| 3    | IdentityService   | 流程角色数据管理，如用户组、用户以及它们之间的关系 |
| 4    | formService       | 表单服务                                           |
| 5    | RuntimeService    | 在流程运行时对流程实例进行管理与控制               |
| 6    | ManagementService | 对流程引擎进行管理与维护                           |
| 7    | HistoryService    | 流程历史数据、查询、删除                           |
| 8    | DynamicBpmSerivce | 动态流程定义模型修改                               |

## IdentityService

- 管理用户(User)
- 管理用户组(UserGroup)
- 用户与用户组的关系(MemberShip) 多对多

```java
public interface IdentityService {

  /**
   * 创建一个新用户
   */
  User newUser(String userId);

  /**
   * 保存用户
   */
  void saveUser(User user);

  /**
   * Creates a {@link UserQuery} that allows to programmatically query the users.
   */
  UserQuery createUserQuery();

  /**
   * Returns a new {@link org.activiti.engine.query.NativeQuery} for tasks.
   */
  NativeUserQuery createNativeUserQuery();

  /**
   * 删除用户
   */
  void deleteUser(String userId);

  /**
   * 创建用户组
   */
  Group newGroup(String groupId);

  /**
   * 创建用户组查询
   */
  GroupQuery createGroupQuery();

  /**
   * Returns a new {@link org.activiti.engine.query.NativeQuery} for tasks.
   */
  NativeGroupQuery createNativeGroupQuery();

  /**
   * Saves the group. If the group already existed, the group is updated.
   * 保存一个 用户组,存在即更新
   */
  void saveGroup(Group group);

  /**
   * Deletes the group. When no group exists with the given id, this operation is ignored.
   * 删除用户组,不存在忽略
   */
  void deleteGroup(String groupId);

  /**
   * 创建一个用户用户组关联
   */
  void createMembership(String userId, String groupId);

  /**
   * 删除用户和用户组关联
   */
  void deleteMembership(String userId, String groupId);

  /**
   * 判断用户密码
   */
  boolean checkPassword(String userId, String password);

  /**
   * Passes the authenticated user id for this particular thread. All service method (from any service) invocations done by the same thread will have access to this authenticatedUserId.
   */
  void setAuthenticatedUserId(String authenticatedUserId);

  /**
   * Sets the picture for a given user.
   */
  void setUserPicture(String userId, Picture picture);

  /**
   * Retrieves the picture for a given user.
   */
  Picture getUserPicture(String userId);

  /** Generic extensibility key-value pairs associated with a user */
  void setUserInfo(String userId, String key, String value);

  /** Generic extensibility key-value pairs associated with a user */
  String getUserInfo(String userId, String key);

  /** Generic extensibility keys associated with a user */
  List<String> getUserInfoKeys(String userId);

  /**
   * Delete an entry of the generic extensibility key-value pairs associated with a user
   */
  void deleteUserInfo(String userId, String key);
}

```