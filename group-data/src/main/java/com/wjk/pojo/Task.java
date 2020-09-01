package com.wjk.pojo;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task implements Serializable {

    private Lock lock = new ReentrantLock ();
    private Condition condition = lock.newCondition();
    private Boolean flag = false;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void waitTask() {
        lock.lock ();
        try {
            this.flag = true;
            condition.await ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } finally {
            lock.unlock ();
        }
    }

    public void signalTask() {
        lock.lock ();
        try {
            condition.signalAll ();
            this.flag = false;
        }catch (Exception e){
            e.printStackTrace ();
        } finally {
            lock.unlock ();
        }
    }
}
