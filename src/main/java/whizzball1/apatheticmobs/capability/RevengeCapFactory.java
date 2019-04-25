package whizzball1.apatheticmobs.capability;

import java.util.concurrent.Callable;

public class RevengeCapFactory implements Callable<IRevengeCap> {

    @Override
    public IRevengeCap call() throws Exception {
        return new RevengeCapability();
    }
}
