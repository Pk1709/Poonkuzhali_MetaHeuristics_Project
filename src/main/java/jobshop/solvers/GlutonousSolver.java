package jobshop.solvers;

import jobshop.Instance;
import jobshop.Result;
import jobshop.Solver;
import jobshop.encodings.ResourceOrder;
import jobshop.encodings.Task;
import java.util.Arrays;


public class GlutonousSolver implements Solver {


}



@Override
public Result solve( Instance instance, long deadline) {

    int[][] maintain = new[instance.numJobs][instance.numTasks];
    for (int[] row : maintain)
        Arrays.fill(row, 0);
// tasks that has already maintained without searching for resources
    ResourceOrder solution = new ResourceOrder(instance);
    Task t = later(instance, maintain, solution);
    while (t != null) {
        int m = instance.machine(t.job, t.task);
        solution.tasksByMachine[m][solution.nextFreeSlot]++ = t;
        t = later(instance, maintain, solution);
    }
    return new Result(instance, solution.toSchedule(), Result.ExitCause.Blocked);
}

    private Task later(Instance instance, int[][] maintain, ResourceOrder solution) {
    for(int j=0; j<instance.numJobs;j++)
        for(int t =0; t<instance.numTasks; t++){

        if( maintain[j][t]==0){
            //adding the new task
        }
    }


// choosing the task based on the processing time

switch() {
    case SPT:

    case LRPT:

}
            
