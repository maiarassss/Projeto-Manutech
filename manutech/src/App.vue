<script setup>
import { ref } from "vue";
import { RouterLink, RouterView } from "vue-router";

const sidebarOpen = ref(false);
</script>

<template>
  <header class="navbar">
    <button class="menu-button" @click="sidebarOpen = !sidebarOpen">☰</button>

    <h1>MANUTECH</h1>

    <input type="text" placeholder="Pesquisar..." class="search-bar" />
  </header>

  <div class="container">
    <!-- Sidebar -->
    <aside class="sidebar" :class="{ active: sidebarOpen }">
      <RouterLink to="/"> Início </RouterLink>

      <RouterLink to="/tecnicos"> Técnicos </RouterLink>

      <RouterLink to="/ordens"> Ordens </RouterLink>

      <RouterLink to="/maquinas"> Máquinas </RouterLink>
    </aside>

    <main class="content" :class="{ shifted: sidebarOpen }">
      <RouterView />
    </main>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family:
    sans-serif,
    Segoe UI;
  background: #f4f6f9;
}

/*NAVBAR*/

.navbar {
  height: 70px;
  background: #144e94;

  display: flex;
  align-items: center;
  justify-content: space-between;

  padding: 0 30px;

  color: white;
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

.container {
  display: flex;
}

.sidebar {
  position: fixed;

  top: 70px;
  left: -250px;

  width: 250px;
  height: 100vh;

  background: #1e293b;

  display: flex;
  flex-direction: column;

  padding-top: 30px;

  transition: 0.3s;
}

.sidebar.active {
  left: 0;
}

.sidebar a {
  color: white;

  text-decoration: none;

  padding: 20px;

  font-size: 18px;

  transition: 0.3s;
}

.sidebar a:hover {
  background: #334155;
}

.sidebar a.router-link-active {
  background: #1565c0;
  font-weight: bold;
}

.content {
  width: 100%;

  padding: 40px;

  transition: 0.3s;
}

.content.shifted {
  margin-left: 250px;
}
</style>
