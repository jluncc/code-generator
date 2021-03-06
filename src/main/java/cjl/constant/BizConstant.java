package cjl.constant;

/**
 * 常用变量
 * <p>
 * Created by jinglun on 2020-03-10
 */
public interface BizConstant {
    interface ORM {
        String MyBatis = "mybatis";
        String JPA = "jpa";
    }

    interface GENERATE_STRATEGY {
        String ENTITY = "entity";
        String DAO = "dao";
        String SERVICE = "service";
        String CONTROLLER = "controller";
    }
}
