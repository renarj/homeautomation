package com.oberasoftware.home.core.model.storage;

import com.oberasoftware.home.api.model.storage.Dashboard;
import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

/**
 * @author Renze de Vries
 */
@JasDBEntity(bagName = "dashboards")
public class DashboardImpl implements Dashboard {

    private String id;
    private String name;
    private long weight;

    public DashboardImpl(String id, String name, long weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    public DashboardImpl() {
    }

    @Override
    @Id
    @JasDBProperty
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JasDBProperty
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JasDBProperty
    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }
}
