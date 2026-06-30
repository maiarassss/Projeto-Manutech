<script setup>
import { ref, computed } from "vue";
import { RouterLink, RouterView, useRoute } from "vue-router";
import { Menu, Home, Users, Wrench, Cpu, UserCircle } from "lucide-vue-next";

const route = useRoute();

const sidebarOpen = ref(false);

const ehTelaLogin = computed(() => route.path === "/login");

function abrirSidebar() {
  sidebarOpen.value = true;
}

function alternarSidebar() {
  sidebarOpen.value = !sidebarOpen.value;
}

function fecharSidebar() {
  sidebarOpen.value = false;
}
</script>

<template>
  <template v-if="!ehTelaLogin">
    <header class="navbar">
      <button class="menu-button" @click="alternarSidebar">
        <Menu :size="28" />
      </button>

      <h1>MANUTECH</h1>

      <RouterLink to="/login" class="login-atalho">
        <UserCircle :size="34" />
      </RouterLink>
    </header>

    <aside
      class="sidebar"
      :class="{ aberta: sidebarOpen }"
      @click="abrirSidebar"
    >
      <RouterLink
        to="/"
        class="item-menu"
        active-class="ativo"
        @click.stop="fecharSidebar"
      >
        <Home :size="22" />
        <span v-if="sidebarOpen">Início</span>
      </RouterLink>

      <RouterLink
        to="/tecnicos"
        class="item-menu"
        active-class="ativo"
        @click.stop="fecharSidebar"
      >
        <Users :size="22" />
        <span v-if="sidebarOpen">Técnicos</span>
      </RouterLink>

      <RouterLink
        to="/ordens"
        class="item-menu"
        active-class="ativo"
        @click.stop="fecharSidebar"
      >
        <Wrench :size="22" />
        <span v-if="sidebarOpen">Ordens</span>
      </RouterLink>

      <RouterLink
        to="/maquinas"
        class="item-menu"
        active-class="ativo"
        @click.stop="fecharSidebar"
      >
        <Cpu :size="22" />
        <span v-if="sidebarOpen">Máquinas</span>
      </RouterLink>
    </aside>

    <main class="content">
      <div class="page-wrapper">
        <RouterView />
      </div>
    </main>
  </template>

  <template v-else>
    <RouterView />
  </template>
</template>

<style>
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  font-family: "Segoe UI", sans-serif;
  background: #f4f6f9;
}

/* NAVBAR */

.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;

  height: 70px;

  background: #144e94;
  color: white;

  display: flex;
  align-items: center;
  justify-content: space-between;

  padding: 0 30px;

  z-index: 1000;
}

.navbar h1 {
  flex: 1;
  margin-left: 25px;

  color: white;
  font-size: 1.8rem;
  letter-spacing: 1px;
}

.menu-button {
  background: none;
  border: none;
  color: white;

  display: flex;
  align-items: center;
  justify-content: center;

  cursor: pointer;
}

.login-atalho {
  color: white;

  display: flex;
  align-items: center;
  justify-content: center;

  transition: 0.2s;
}

.login-atalho:hover {
  opacity: 0.8;
}

/* SIDEBAR */

.sidebar {
  position: fixed;
  top: 70px;
  left: 0;

  width: 70px;
  height: calc(100vh - 70px);

  background: #1e293b;

  display: flex;
  flex-direction: column;
  gap: 14px;

  padding: 20px 12px;

  transition: width 0.25s ease;

  overflow: hidden;
  cursor: pointer;

  z-index: 900;
}

.sidebar.aberta {
  width: 250px;
}

.item-menu {
  min-height: 48px;

  color: white;
  text-decoration: none;

  display: flex;
  align-items: center;
  gap: 14px;

  padding: 12px;

  border-radius: 8px;

  white-space: nowrap;

  transition: background 0.2s;
}

.item-menu:hover {
  background: #334155;
}

.item-menu.ativo {
  background: #1565c0;
  font-weight: bold;
}

.item-menu span {
  font-size: 1rem;
}

/* CONTEÚDO CENTRALIZADO */

.content {
  margin-top: 70px;
  margin-left: 70px;

  width: calc(100vw - 70px);
  min-height: calc(100vh - 70px);

  padding: 40px;

  background: #f4f6f9;

  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.page-wrapper {
  width: min(100%, 1150px);
  margin-left: auto;
  margin-right: auto;
}
</style>
