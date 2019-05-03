package whizzball1.apatheticmobs.rules;

import java.util.ArrayList;
import java.util.List;

public class Rules {
    public List<Rule> defaultRules = new ArrayList<Rule>();

    {
        defaultRules.add(new PlayerWhitelistRule());
        defaultRules.add(new DifficultyLockRule());
        defaultRules.add(new TargeterTypeRule());
        defaultRules.add(new RevengeRule());
        defaultRules.sort(new Rule.RuleComparator());
    }
}
