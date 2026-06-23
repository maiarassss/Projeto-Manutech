<script setup>
import { ref } from "vue";
import { RouterLink, RouterView } from "vue-router";

const sidebarOpen = ref(true);
</script>

<template>
  <header class="navbar">
    <button class="menu-button" @click="sidebarOpen = !sidebarOpen">☰</button>
    <h1>MANUTECH</h1>
    <input type="text" placeholder="Pesquisar..." class="search-bar" />
  </header>

  <aside class="sidebar" :class="{ collapsed: !sidebarOpen }">
    <RouterLink to="/">Início</RouterLink>
    <RouterLink to="/tecnicos">Técnicos</RouterLink>
    <RouterLink to="/ordens">Ordens</RouterLink>
    <RouterLink to="/maquinas">Máquinas</RouterLink>
  </aside>

  <main class="content" :class="{ shifted: sidebarOpen }">
    <RouterView />
  </main>
</template>

<style>
body {
  font-family:
    sans-serif,
    Segoe UI;
  background: #f4f6f9;
}

/*NAVBAR*/

.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 70px;
  background: #144e94;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  color: white;
  z-index: 1000;
}

.navbar h1 {
  flex: 1;
  margin-left: 13%;
  color: white;
  height: 130%;
}

.menu-button {
  background: none;
  border: none;
  color: white;
  font-size: 28px;
  cursor: pointer;
}

.search-bar {
  width: 280px;
  height: 40px;
  border: none;
  border-radius: 10px;
  padding-left: 15px;
  outline: none;
}

/* CORPO DA PÁGINA */

.sidebar {
  position: fixed;
  top: 70px;
  left: 0;
  height: calc(100vh - 70px);

  width: 250px;

  background: #1e293b;

  display: flex;
  flex-direction: column;
  padding: 20px;
  gap: 20px;

  transition: width 0.3s ease;
  overflow: hidden;
}

/* estado recolhido */
.sidebar.collapsed {
  width: 70px;
}

.sidebar.collapsed {
  width: 70px;
}

.sidebar a {
  color: white;
  text-decoration: none;
  font-size: 18px;

  display: flex;
  align-items: center;
  gap: 10px;

  padding: 12px;
  border-radius: 6px;

  white-space: nowrap;
  overflow: hidden;
}

.sidebar a:hover {
  background: #334155;
}

.sidebar a.router-link-active {
  background: #1565c0;
  font-weight: bold;
}
.sidebar.collapsed a {
  justify-content: center;
  font-size: 0;
}

.content {
  margin-top: 70px;
  padding: 40px;

  margin-left: 250px;

  transition: none;
}
</style>
