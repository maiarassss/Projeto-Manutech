<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();

// campos do formulário
const modelo = ref("");
const responsavel = ref("");
const status = ref("");

//função pra diferenciar se é cadastro ou edição
const isEdit = ref(false);

onMounted(() => {
  const id = route.params.id;

  if (id) {
    isEdit.value = true;

    console.log("Editando máquina ID:", id);

    modelo.value = "Máquina A";
    responsavel.value = "João";
    status.value = "Ativa";
  }
});

//salva mas ainda n tem backend
function salvar() {
  if (isEdit.value) {
    console.log("Atualizando máquina...");
    console.log({
      modelo: modelo.value,
      responsavel: responsavel.value,
      status: status.value,
    });
  } else {
    console.log("Criando máquina...");
    console.log({
      modelo: modelo.value,
      responsavel: responsavel.value,
      status: status.value,
    });
  }
}
</script>

<template>
  <div>
    <h1>
      {{ isEdit ? "Atualizar Máquina" : "Cadastro de Máquinas" }}
    </h1>

    <form class="formulario">
      <input v-model="modelo" type="text" placeholder="Modelo" />

      <select v-model="responsavel">
        <option value="">Selecione o responsável</option>
        <option>João</option>
        <option>Maria</option>
        <option>Carlos</option>
      </select>

      <select v-model="status">
        <option value="">Selecione o status</option>
        <option>Ativa</option>
        <option>Em manutenção</option>
        <option>Inativa</option>
      </select>

      <button type="button" @click="salvar">
        {{ isEdit ? "Atualizar" : "Salvar" }}
      </button>
    </form>
  </div>
</template>

<style scoped>
.formulario {
  display: flex;
  flex-direction: column;
  gap: 15px;
  max-width: 400px;
  margin-left: 27%;
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
