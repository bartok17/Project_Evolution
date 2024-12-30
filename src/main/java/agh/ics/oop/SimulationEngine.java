package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {

    private ExecutorService executorService;

    private final List<Simulation> simulations;
    private final List<Thread> threads = new ArrayList<>();
    public SimulationEngine(List<Simulation> simulations) {
       this.simulations = simulations;
    }
    public void runSync()
    {
        for (Simulation simulation : simulations)
        {
           simulation.run();
        }
    }
    public void runAsync() {

        for (Simulation simulation : simulations) {

            Thread thread = new Thread(simulation);
            threads.add(thread);
            thread.start();
        }

    }
    public void runAsyncInThreadPool() {
        executorService = Executors.newFixedThreadPool(16);
        for (Simulation simulation : simulations) {
            executorService.submit(simulation);
        }

    }

    public void awaitSimulationsEnd() throws InterruptedException {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
        else
        {
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
