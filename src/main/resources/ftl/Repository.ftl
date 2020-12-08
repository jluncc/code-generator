package ${packageName};

import ${entityPackageName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ${tableComment} dao层接口
 *
 * Created by ${author} on ${date}
 */
@Repository
public interface ${tableName}Repository extends JpaRepository<${tableName}, Integer> {
}