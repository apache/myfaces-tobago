/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class TobagoConfigSorter {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final List<Vertex> vertices = new ArrayList<>();

  public static void sort(List<TobagoConfigFragment> fragments) {
    TobagoConfigSorter sorter = new TobagoConfigSorter(fragments);
    final List<TobagoConfigFragment> sorted = sorter.topologicalSort();
    fragments.clear();
    fragments.addAll(sorted);
  }

  private TobagoConfigSorter(final List<TobagoConfigFragment> fragmentList) {
    for (TobagoConfigFragment tobagoConfigFragment : fragmentList) {
      vertices.add(new Vertex(tobagoConfigFragment));
    }
  }

  /**
   * Topological sorting with setup and cycle check.
   *
   * @throws IllegalStateException When detecting a cycle.
   */
  private List<TobagoConfigFragment> topologicalSort() {

    createEdges();
    checkCycles();

    List<TobagoConfigFragment> result = new ArrayList<>();

    for (Vertex vertex : vertices) {
      topologicalSort0(vertex, result);
    }

    logResult(result);

    return result;
  }

  /**
   * Internal recursive method for the topological sort.
   */
  private void topologicalSort0(Vertex vertex, List<TobagoConfigFragment> result) {
    if (vertex.isVisited()) {
      return;
    }

    vertex.setVisited(true);

    // recursion for all vertices adjacent to this vertex
    for (Vertex adjacent : vertex.getAdjacencyList()) {
      topologicalSort0(adjacent, result);
    }

    result.add(vertex.getFragment());
  }

  private void logResult(List<TobagoConfigFragment> result) {
    if (LOG.isInfoEnabled()) {
      StringBuilder builder = new StringBuilder("Order of the Tobago config files: ");
      for (TobagoConfigFragment fragment : result) {
        final String name = fragment.getName();
        if (LOG.isDebugEnabled()) {
          builder.append("name=");
          if (name == null) {
            builder.append("<unnamed>");
          } else {
            builder.append("'");
            builder.append(name);
            builder.append("'");
          }
          builder.append(" url='");
          builder.append(fragment.getUrl());
          builder.append("'");
        } else {
          builder.append(name);
          builder.append(", ");
        }
      }
      LOG.info(builder.toString());
    }
  }

  private void createEdges() {

    // collecting all relations, which are relevant for us. We don't need "before" and "after" of unknown names.
    for (final Vertex vertex : vertices) {
      final TobagoConfigFragment current = vertex.getFragment();

      for (final String befores : current.getBefore()) {
        final TobagoConfigFragment before = findByName(befores);
        if (before != null) {
          findVertex(before).addAdjacent(findVertex(current));
        }
      }
      for (final String afters : current.getAfter()) {
        final TobagoConfigFragment after = findByName(afters);
        if (after != null) {
          findVertex(current).addAdjacent(findVertex(after));
        }
      }
    }
  }

  /**
   * Cycle detection: if the base in reachable form its own, than there is a cycle.
   *
   * @throws IllegalStateException When detecting a cycle.
   */
  private void checkCycles() {
    LOG.debug("Cycle detection:");
    for (Vertex vertex : vertices) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Checking reachable vertices from base {}", vertex.getFragment());
      }
      checkCycles0(vertex, vertex);
    }
  }

  private void checkCycles0(final Vertex vertex, final Vertex base) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("vertex: {}", vertex.toString());
      LOG.debug("base:   {}", base.getFragment().toString());
    }
    for (Vertex adjacent : vertex.getAdjacencyList()) {
      if (base == adjacent) {
        throw new IllegalStateException("Cycle detected name='" + vertex + "' base=" + base + "! ");
      }
      checkCycles0(adjacent, base);
    }
  }

  private Vertex findVertex(final TobagoConfigFragment fragment) {
    for (Vertex vertex : vertices) {
      if (vertex.getFragment() == fragment) {
        return vertex;
      }
    }
    throw new RuntimeException("Problem with sorting! This might be a bug.");
  }

  private TobagoConfigFragment findByName(final String name) {
    for (final Vertex vertex : vertices) {
      TobagoConfigFragment fragment = vertex.getFragment();
      if (name.equals(fragment.getName())) {
        return fragment;
      }
    }
    return null;
  }

  private static class Vertex {

    private final TobagoConfigFragment fragment;
    private final List<Vertex> adjacencyList;
    private boolean visited;

    private Vertex(final TobagoConfigFragment fragment) {
      this.fragment = fragment;
      this.adjacencyList = new ArrayList<>();
    }

    public boolean isVisited() {
      return visited;
    }

    public void setVisited(boolean visited) {
      this.visited = visited;
    }

    public TobagoConfigFragment getFragment() {
      return fragment;
    }

    public void addAdjacent(Vertex higher) {
      adjacencyList.add(higher);
    }

    public List<Vertex> getAdjacencyList() {
      return adjacencyList;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(fragment);
      builder.append(" -> [");
      for (Vertex vertex : adjacencyList) {
        builder.append(vertex.getFragment());
        builder.append(", ");
      }
      if (builder.charAt(builder.length() - 1) == ' ') {
        builder.delete(builder.length() - 2, builder.length());
      }
      builder.append("]");
      return builder.toString();
    }
  }
}
