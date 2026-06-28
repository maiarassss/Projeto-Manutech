<template>
  <aside class="sidebar">
    <RouterLink to="/">Início</RouterLink>
    <RouterLink to="/tecnicos">Técnicos</RouterLink>
    <RouterLink to="/ordens">Ordens</RouterLink>
    <RouterLink to="/maquinas">Máquinas</RouterLink>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 230px;
  min-height: calc(100vh - 80px);

  position: fixed;
  top: 70px;
  left: 0px;

  background: #111184;

  display: flex;
  flex-direction: column;
  padding: 80px;
  gap: 20px;
}
a {
  color: white;
  text-decoration: none;
  font-size: 1.1rem;
}

a:hover {
  color: #cbd5e1;
}
</style>
