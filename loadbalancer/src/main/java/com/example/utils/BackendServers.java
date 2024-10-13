package org.example.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BackendServers {

  public enum LoadBalancingStrategy {
    ROUND_ROBIN, LEAST_CONNECTIONS
  }

  private static List<String> servers = new ArrayList<>();
  private static Map<String, Integer> connections = new HashMap<>();
  private static int roundRobinCounter = 0;
  private static LoadBalancingStrategy strategy = LoadBalancingStrategy.ROUND_ROBIN;

  // static {
  // servers.add("IP1");
  // servers.add("IP2");
  // servers.forEach(server -> connections.put(server, 0));
  // }

  // Load config file
  static {
    loadConfiguration("config.json");
  }

  private static void loadConfiguration(String configFile) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      Map<String, Object> config = objectMapper.readValue(new File(configFile), Map.class);
      initializeConfig(config);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load JSON config", e);
    }
  }

  private static void initializeConfig(Map<String, Object> config) {
    servers = (List<String>) config.get("servers");
    for (String server : servers) {
      connections.put(server, 0);
    }

    String strategyName = (String) config.get("strategy");
    strategy = LoadBalancingStrategy.valueOf(strategyName.toUpperCase());
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
