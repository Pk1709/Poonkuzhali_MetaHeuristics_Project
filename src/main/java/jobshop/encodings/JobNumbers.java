package jobshop.encodings;

import jobshop.Encoding;
import jobshop.Instance;
import jobshop.Schedule;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

/** Représentation par numéro de job. */
public class JobNumbers extends Encoding {

    /** A numJobs * numTasks array containing the representation by job numbers. */
    public final int[] jobs;

    /** In case the encoding is only partially filled, indicates the index of the first
     * element of `jobs` that has not been set yet. */
    public int nextToSet = 0;

    public JobNumbers(Instance instance) {
        super(instance);

        jobs = new int[instance.numJobs * instance.numMachines];
        Arrays.fill(jobs, -1);
    }

    public JobNumbers(Schedule schedule) {
        super(schedule.pb);

        this.jobs = new int[instance.numJobs * instance.numTasks];

        // for each job indicates which is the next task to be scheduled
        int[] nextOnJob = new int[instance.numJobs];

        while(Arrays.stream(nextOnJob).anyMatch(t -> t < instance.numTasks)) {
            Task next = IntStream
                    // for all jobs numbers
                    .range(0, instance.numJobs)
                    // build the next task for this job
                    .mapToObj(j -> new Task(j, nextOnJob[j]))
                    // only keep valid tasks (some jobs have no task left to be executed)
                    .filter(t -> t.task < instance.numTasks)
                    // select the task with the earliest execution time
                    .min(Comparator.comparing(t -> schedule.startTime(t.job, t.task)))
                    .get();

            this.jobs[nextToSet++] = next.job;
            nextOnJob[next.job] += 1;
        }
    }

    @Override
    public Schedule toSchedule() {
        // time at which each machine is going to be freed
        int[] nextFreeTimeResource = new int[instance.numMachines];

        // for each job, the first task that has not yet been scheduled
        int[] nextTask = new int[instance.numJobs];

        // for each task, its start time
        int[][] startTimes = new int[instance.numJobs][instance.numTasks];

        // compute the earliest start time for every task of every job
        for(int job : jobs) {
            int task = nextTask[job];
            int machine = instance.machine(job, task);
            // earliest start time for this task
            int est = task == 0 ? 0 : startTimes[job][task-1] + instance.duration(job, task-1);
            est = Math.max(est, nextFreeTimeResource[machine]);

            startTimes[job][task] = est;
            nextFreeTimeResource[machine] = est + instance.duration(job, task);
            nextTask[job] = task + 1;
        }

        return new Schedule(instance, startTimes);
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(jobs,0, nextToSet));
    }
}
