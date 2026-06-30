<script setup>
import { Pencil, XCircle, Eye } from "lucide-vue-next";
import { useOrdensStore } from "@/stores/ordemStore";
import { onMounted, computed, ref } from "vue";

const store = useOrdensStore();

const pesquisa = ref("");
const statusSelecionado = ref("");
const prioridadeSelecionada = ref("");

const mostrarConfirmacao = ref(false);
const ordemParaCancelar = ref(null);

const mostrarDetalhes = ref(false);
const ordemParaDetalhes = ref(null);

onMounted(() => {
  store.buscarOrdens();
});

const ordens = computed(() => store.ordens);

const ordensFiltradas = computed(() => {
  return ordens.value.filter((ordem) => {
    const termo = pesquisa.value.toLowerCase();

    const atendePesquisa =
      !pesquisa.value ||
      ordem.titulo.toLowerCase().includes(termo) ||
      ordem.descricao.toLowerCase().includes(termo) ||
      ordem.modelo.toLowerCase().includes(termo) ||
      ordem.codigoIdentificador.toLowerCase().includes(termo) ||
      ordem.nomeSetor.toLowerCase().includes(termo) ||
      ordem.nomeTecnico.toLowerCase().includes(termo);

    const atendeStatus =
      !statusSelecionado.value || ordem.status === statusSelecionado.value;

    const atendePrioridade =
      !prioridadeSelecionada.value ||
      ordem.prioridade === prioridadeSelecionada.value;

    return atendePesquisa && atendeStatus && atendePrioridade;
  });
});

function abrirDetalhes(ordem) {
  ordemParaDetalhes.value = ordem;
  mostrarDetalhes.value = true;
}

function fecharDetalhes() {
  mostrarDetalhes.value = false;
  ordemParaDetalhes.value = null;
}

function confirmarCancelamento(ordem) {
  ordemParaCancelar.value = ordem;
  mostrarConfirmacao.value = true;
}

async function cancelarOrdem() {
  await store.cancelarOrdem(ordemParaCancelar.value.idOrdem);

  mostrarConfirmacao.value = false;
  ordemParaCancelar.value = null;
}

function fecharConfirmacao() {
  mostrarConfirmacao.value = false;
  ordemParaCancelar.value = null;
}

function limparFiltros() {
  pesquisa.value = "";
  statusSelecionado.value = "";
  prioridadeSelecionada.value = "";
}
</script>

<template>
  <div class="container">
    <h1>Ordens de Manutenção</h1>

    <div class="barra-filtros">
      <input
        v-model="pesquisa"
        type="text"
        placeholder="Pesquisar por título, máquina, setor ou técnico..."
      />

      <select v-model="statusSelecionado">
        <option value="">Todos os status</option>
        <option value="EM_ANDAMENTO">Em andamento</option>
        <option value="CONCLUIDA">Concluída</option>
        <option value="CANCELADA">Cancelada</option>
      </select>

      <select v-model="prioridadeSelecionada">
        <option value="">Todas as prioridades</option>
        <option value="BAIXA">Baixa</option>
        <option value="MEDIA">Média</option>
        <option value="ALTA">Alta</option>
        <option value="CRITICA">Crítica</option>
      </select>

      <button class="btn-limpar" @click="limparFiltros">Limpar filtros</button>
    </div>

    <div class="contador">{{ ordensFiltradas.length }} ordens encontradas</div>

    <table class="tabela-ordens">
      <thead>
        <tr>
          <th>Título</th>
          <th>Máquina</th>
          <th>Setor</th>
          <th>Técnico</th>
          <th>Prioridade</th>
          <th>Status</th>
          <th>Ações</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="ordem in ordensFiltradas" :key="ordem.idOrdem">
          <td>{{ ordem.titulo }}</td>

          <td>{{ ordem.codigoIdentificador }} - {{ ordem.modelo }}</td>

          <td>{{ ordem.nomeSetor }}</td>

          <td>{{ ordem.nomeTecnico }}</td>

          <td>
            <span class="prioridade" :class="ordem.prioridade.toLowerCase()">
              {{ ordem.prioridade }}
            </span>
          </td>

          <td>
            <span class="status" :class="ordem.status.toLowerCase()">
              {{ ordem.status }}
            </span>
          </td>

          <td class="acoes">
            <button
              class="detalhes"
              type="button"
              @click="abrirDetalhes(ordem)"
            >
              <Eye :size="18" />
            </button>

            <router-link :to="`/ordens/editar/${ordem.idOrdem}`">
              <button class="editar" type="button">
                <Pencil :size="18" />
              </button>
            </router-link>

            <button
              class="cancelar-ordem"
              type="button"
              @click="confirmarCancelamento(ordem)"
              :disabled="ordem.status === 'CANCELADA'"
            >
              <XCircle :size="18" />
            </button>
          </td>
        </tr>
        <tr v-if="ordensFiltradas.length === 0">
          <td colspan="7" class="sem-resultados">Nenhuma ordem encontrada.</td>
        </tr>
      </tbody>
    </table>

    <router-link to="/ordens/cadastro">
      <button class="btn-cadastro">+ Cadastrar Ordem</button>
    </router-link>

    <div v-if="mostrarDetalhes" class="overlay">
      <div class="modal detalhes-modal">
        <h2>Detalhes da Ordem</h2>

        <div class="conteudo-detalhes">
          <p>
            <strong>Título:</strong>
            {{ ordemParaDetalhes?.titulo }}
          </p>

          <p>
            <strong>Descrição:</strong>
            {{ ordemParaDetalhes?.descricao }}
          </p>

          <p>
            <strong>Máquina:</strong>
            {{ ordemParaDetalhes?.codigoIdentificador }} -
            {{ ordemParaDetalhes?.modelo }}
          </p>

          <p>
            <strong>Setor:</strong>
            {{ ordemParaDetalhes?.nomeSetor }}
          </p>

          <p>
            <strong>Técnico responsável:</strong>
            {{ ordemParaDetalhes?.nomeTecnico }}
          </p>

          <p>
            <strong>Prioridade:</strong>
            {{ ordemParaDetalhes?.prioridade }}
          </p>

          <p>
            <strong>Status:</strong>
            {{ ordemParaDetalhes?.status }}
          </p>
        </div>

        <div class="botoes-modal">
          <button class="cancelar" @click="fecharDetalhes">Fechar</button>
        </div>
      </div>
    </div>

    <div v-if="mostrarConfirmacao" class="overlay">
      <div class="modal">
        <h2>Cancelar ordem</h2>

        <p>
          Deseja realmente cancelar a ordem
          <strong>{{ ordemParaCancelar?.titulo }}</strong
          >?
        </p>

        <div class="botoes-modal">
          <button class="cancelar" @click="fecharConfirmacao">Voltar</button>

          <button class="confirmar" @click="cancelarOrdem">
            Cancelar ordem
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 95%;
  max-width: 1300px;
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

.barra-filtros input {
  min-width: 350px;
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

.tabela-ordens {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
}

.tabela-ordens thead {
  background: #173f82;
  color: white;
}

.tabela-ordens th,
.tabela-ordens td {
  padding: 16px;
  text-align: center;
}

.tabela-ordens tbody tr {
  border-bottom: 1px solid #ececec;
}

.tabela-ordens tbody tr:hover {
  background: #f7f9fc;
}

.prioridade,
.status {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: bold;
}

.baixa {
  background: #d7f6de;
  color: #1d7d38;
}

.media {
  background: #fff1c2;
  color: #8a6d00;
}

.alta {
  background: #ffd9b3;
  color: #b85c00;
}

.critica {
  background: #ffd9d9;
  color: #b42318;
}

.andamento {
  background: #dbeafe;
  color: #1d4ed8;
}

.em_andamento {
  background: #dbeafe;
  color: #1d4ed8;
}

.concluida {
  background: #d7f6de;
  color: #1d7d38;
}

.cancelada {
  background: #e5e5e5;
  color: #555;
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

.detalhes {
  color: #173f82;
}

.editar {
  color: #173f82;
}

.cancelar-ordem {
  color: #d62828;
}

.cancelar-ordem:disabled {
  color: #aaa;
  cursor: not-allowed;
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

  width: 420px;

  padding: 30px;

  border-radius: 12px;

  text-align: center;

  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}

.modal h2 {
  color: #173f82;
}

.detalhes-modal {
  text-align: left;
}

.detalhes-modal h2 {
  text-align: center;
}

.conteudo-detalhes {
  display: flex;
  flex-direction: column;
  gap: 10px;

  margin-top: 20px;
}

.conteudo-detalhes p {
  margin: 0;
  color: #333;
  line-height: 1.4;
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

.sem-resultados {
  padding: 30px;
  color: #777;
  font-weight: bold;
  text-align: center;
}
</style>
