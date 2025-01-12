import edu.moonlightmoth.HiStat.service.StudentsTable;
import org.junit.jupiter.api.Test;

public class StudetsTableTest {

    @Test
    void lookupTest()
    {
        System.out.println(StudentsTable.lookupCriticVal(0.12, 34));
        System.out.println(StudentsTable.lookupCriticVal(0.00015, 100));
        System.out.println(StudentsTable.lookupCriticVal(0.099, 300));
        System.out.println(StudentsTable.lookupCriticVal(0.001, 60));
    }
}
