import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ecode.EServerApplication;
import com.ecode.entity.ClassScore;
import com.ecode.entity.po.CodeSubmission;
import com.ecode.mapper.ClassScoreMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = EServerApplication.class)
public class Test1 {

    @Autowired
    private ClassScoreMapper classScoreMapper;


    @Test
    public void test() {
        CodeSubmission submission = CodeSubmission.builder()
                .codeText("print(1)\n")
                .submitTime(LocalDateTime.now())
                .languageType("python")
                .passCount(0)
                .build();

        int i = classScoreMapper.update(null, new LambdaUpdateWrapper<ClassScore>()
                .set(ClassScore::getScore, 2)
                .set(ClassScore::getCodeSubmission, List.of(submission), "typeHandler=com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler") // 更新代码历史提交记录
                .setSql("submit_number = submit_number + 1")
                .setSql(3 == 4, "pass_number = pass_number + 1")
                .eq(ClassScore::getScId, 53)
                .eq(ClassScore::getClassProblemId, 17)
        );
    }
}
