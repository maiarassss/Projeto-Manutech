<script setup>
import { Pencil, Trash2 } from "lucide-vue-next";
import { useMaquinasStore } from "@/stores/maquinaStore";
import { onMounted, computed, ref } from "vue";

const store = useMaquinasStore();

const pesquisa = ref("");
const modeloSelecionado = ref("");
const setorSelecionado = ref("");
const mostrarConfirmacao = ref(false);
const maquinaParaExcluir = ref(null);

onMounted(() => {
  store.buscarMaquinas();
});

const maquinas = computed(() => store.maquinas);

const modelos = computed(() => {
  return [
    ...new Set(maquinas.value.map((maquina) => maquina.modelo).filter(Boolean)),
  ];
});

const setores = computed(() => {
  return [
    ...new Set(
      maquinas.value.map((maquina) => maquina.nomeSetor).filter(Boolean),
    ),
  ];
});

const maquinasFiltradas = computed(() => {
  return maquinas.value.filter((maquina) => {
    const atendePesquisa =
      !pesquisa.value ||
      (maquina.codigoIdentificador || maquina.nome)
        ?.toLowerCase()
        .includes(pesquisa.value.toLowerCase()) ||
      maquina.modelo?.toLowerCase().includes(pesquisa.value.toLowerCase());

    const atendeModelo =
      !modeloSelecionado.value || maquina.modelo === modeloSelecionado.value;

    const atendeSetor =
      !setorSelecionado.value || maquina.nomeSetor === setorSelecionado.value;

    return atendePesquisa && atendeModelo && atendeSetor;
  });
});

function confirmarExclusao(maquina) {
  maquinaParaExcluir.value = maquina;
  mostrarConfirmacao.value = true;
}

async function excluirMaquina() {
  await store.excluirMaquina(maquinaParaExcluir.value.idMaquina);

  mostrarConfirmacao.value = false;
  maquinaParaExcluir.value = null;
}

function cancelarExclusao() {
  mostrarConfirmacao.value = false;
  maquinaParaExcluir.value = null;
}

function limparFiltros() {
  pesquisa.value = "";
  modeloSelecionado.value = "";
  setorSelecionado.value = "";
}
</script>
<template>
  <div class="container">
    <h1>Máquinas</h1>
    <div class="barra-filtros">
      <input
        v-model="pesquisa"
        type="text"
        placeholder="Pesquisar por código ou modelo..."
      />

      <select v-model="modeloSelecionado">
        <option value="">Todos os modelos</option>

        <option v-for="modelo in modelos" :key="modelo" :value="modelo">
          {{ modelo }}
        </option>
      </select>

      <select v-model="setorSelecionado">
        <option value="">Todos os setores</option>

        <option v-for="setor in setores" :key="setor" :value="setor">
          {{ setor }}
        </option>
      </select>

      <button class="btn-limpar" @click="limparFiltros">
        Limpar filtros selecionados
      </button>
    </div>

    <div class="contador">
      {{ maquinasFiltradas.length }} máquinas encontradas
    </div>

    <table class="tabela-maquinas">
      <thead>
        <tr>
          <th>Código</th>
          <th>Modelo</th>
          <th>Setor</th>
          <th>Status</th>
          <th>Ações</th>
        </tr>
      </thead>

      <tbody>
        <tr
          v-for="maquina in maquinasFiltradas"
          :key="maquina.id || maquina.idMaquina"
        >
          <td>{{ maquina.codigoIdentificador || maquina.nome }}</td>

          <td>{{ maquina.modelo || "-" }}</td>

          <td>{{ maquina.nomeSetor || "-" }}</td>

          <td>
            <span class="status" :class="maquina.ativa ? 'ativa' : 'inativa'">
              {{ maquina.ativa ? "Ativa" : "Inativa" }}
            </span>
          </td>

          <td class="acoes">
            <router-link :to="`/maquinas/editar/${maquina.idMaquina}`">
              <button class="editar" type="button">
                <Pencil :size="18" />
              </button>
            </router-link>

            <button
              class="excluir"
              type="button"
              @click="confirmarExclusao(maquina)"
            >
              <Trash2 :size="18" />
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <router-link to="/maquinas/cadastro">
      <button class="btn-cadastro">+ Cadastrar Máquina</button>
    </router-link>

    <div v-if="mostrarConfirmacao" class="overlay">
      <div class="modal">
        <h2>Confirmar exclusão</h2>

        <p>
          Deseja realmente excluir a máquina
          <strong> {{ maquinaParaExcluir?.modelo }} </strong>?
        </p>

        <div class="botoes-modal">
          <button class="cancelar" @click="cancelarExclusao">Cancelar</button>

          <button class="confirmar" @click="excluirMaquina">Excluir</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 0;

  display: flex;
  flex-direction: column;
  align-items: center;
}

h1 {
  color: #153b7a;
  font-size: 3rem;
  margin-bottom: 30px;
}

.tabela-maquinas {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
}

.tabela-maquinas thead {
  background: #173f82;
  color: white;
}

.tabela-maquinas th,
.tabela-maquinas td {
  padding: 18px;
  text-align: center;
}

.tabela-maquinas tbody tr {
  border-bottom: 1px solid #ececec;
}

.tabela-maquinas tbody tr:hover {
  background: #f7f9fc;
}

.status {
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: bold;
}

.ativa {
  background: #d7f6de;
  color: #1d7d38;
}

.inativa {
  background: #ffd9d9;
  color: #b42318;
}

.acoes {
  display: flex;
  justify-content: center;
  gap: 15px;
}

button {
  border: none;
  background: transparent;
  cursor: pointer;
}

.editar {
  color: #173f82;
}

.excluir {
  color: #d62828;
}

.btn-cadastro {
  margin-top: 35px;
  width: 280px;
  height: 60px;

  background: #173f82;
  color: white;

  border-radius: 8px;

  font-size: 1.1rem;
  font-weight: bold;

  transition: 0.2s;
}

.btn-cadastro:hover {
  background: #123063;
}

.barra-filtros {
  width: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;

  margin-bottom: 25px;

  flex-wrap: wrap;
}

.barra-filtros input,
.barra-filtros select {
  padding: 12px;

  min-width: 220px;

  border: 1px solid #d8d8d8;
  border-radius: 8px;

  font-size: 1rem;
}

.btn-limpar {
  background: #173f82;
  color: white;

  padding: 12px 24px;

  border: none;
  border-radius: 8px;

  cursor: pointer;

  transition: 0.2s;
}

.btn-limpar:hover {
  background: #123063;
}

.contador {
  width: 100%;
  margin-bottom: 15px;

  color: #555;
  font-size: 1rem;
  font-weight: bold;
}

.overlay {
  position: fixed;
  inset: 0;

  background: rgba(0, 0, 0, 0.45);

  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;

  width: 400px;

  padding: 30px;

  border-radius: 12px;

  text-align: center;

  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}

.modal h2 {
  color: #173f82;
}

.botoes-modal {
  display: flex;
  justify-content: center;
  gap: 20px;

  margin-top: 25px;
}

.cancelar,
.confirmar {
  padding: 12px 25px;

  border-radius: 8px;

  font-weight: bold;

  cursor: pointer;
}

.cancelar {
  background: #ddd;
  color: #333;
}

.confirmar {
  background: #d62828;
  color: white;
}
</style>
