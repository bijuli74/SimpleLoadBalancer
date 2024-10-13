package org.example.utils;

import java.util.ArrayList;
import java.util.List;

public class BackendServers {

  public enum LoadBalancingStrategy {
    ROUND_ROBIN, LEAST_CONNECTIONS
  }

  private static List<String> servers = new ArrayList<>();
  private static Map<String, Integer> connections = new HashMap<>();
  private static int roundRobinCounter = 0;
  private static LoadBalancingStrategy strategy = LoadBalancingStrategy.ROUND_ROBIN;

  static {
    servers.add("IP1");
    servers.add("IP2");
    servers.forEach(server -> connections.put(server, 0));
  }

  public static void setStrategy(LoadBalancingStrategy newStrategy) {
    strategy = newStrategy;
  }

  private static String getRoundRobinHost() {
    String host = servers.get(roundRobinCounter % servers.size());
    roundRobinCounter++;
    return host;
  }

  private static String getLeastConnectionsHost() {
    return connections.entrySet().stream()
        .min(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElseThrow(() -> new RuntimeException("No servers available"));
  }

  public static void incrementConnection(String server) {
    connections.put(server, connections.getOrDefault(server, 0) + 1);
  }

  public static void decrementConnection(String server) {
    connections.put(server, Math.max(connections.getOrDefault(server, 1) - 1, 0));
  }

  public static String getHost() {
    switch (strategy) {
      case LEAST_CONNECTIONS:
        return getLeastConnectionsHost();
      case ROUND_ROBIN:
      default:
        return getRoundRobinHost();
    }
  }
}
