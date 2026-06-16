<script setup>
import { ref } from "vue";

const nome = ref("");
const modelo = ref("");
const status = ref("");
</script>

<template>
  <div>
    <h1>Cadastro de Máquinas</h1>

    <form class="formulario">
      <input v-model="nome" type="text" placeholder="Nome da máquina" />

      <input v-model="modelo" type="text" placeholder="Modelo" />

      <select v-model="status">
        <option value="">Selecione o status</option>
        <option>Ativa</option>
        <option>Em manutenção</option>
        <option>Inativa</option>
      </select>

      <button type="button">Salvar</button>
    </form>
  </div>
</template>

<style scoped>
.formulario {
  display: flex;
  flex-direction: column;
  gap: 15px;
  max-width: 400px;
}

input,
select {
  padding: 10px;
}

button {
  padding: 10px;
  cursor: pointer;
}
</style>
