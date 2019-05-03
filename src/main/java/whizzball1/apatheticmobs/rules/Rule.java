package whizzball1.apatheticmobs.rules;

import java.util.Comparator;

public abstract class Rule implements IRule, Comparable<Rule>{

    public int compareTo(Rule rule) {
        return rule.priority() == this.priority() ? 0 : -(this.priority() - rule.priority());
    }

    public static class RuleComparator implements Comparator<Rule> {
        public int compare(Rule a, Rule b) {
            if (a.priority() == b.priority()) return 0;
            return a.priority() - b.priority();
        }

    }
}
