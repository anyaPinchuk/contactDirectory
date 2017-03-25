package services;

import db.ConnectionAwareExecutor;

public interface ServiceEntity {
    ConnectionAwareExecutor connectionAwareExecutor = new ConnectionAwareExecutor();
}
