<template>
  <div class="pagina-ordens">
    <div class="cabecalho-pagina">
      <div>
        <h1>Ordens de manutenção</h1>
        <p>Gerencie as ordens de serviço cadastradas no sistema.</p>
      </div>

      <button class="btn-cadastrar" @click="cadastrarOrdem">
        Cadastrar ordem
      </button>
    </div>

    <div class="card-filtros">
      <div class="campo-pesquisa">
        <label for="pesquisa">Pesquisar</label>
        <input
          id="pesquisa"
          v-model="pesquisa"
          type="text"
          placeholder="Buscar por título, máquina, setor ou técnico..."
        />
      </div>

      <div class="campo-filtro">
        <label for="status">Status</label>
        <select id="status" v-model="filtroStatus">
          <option value="">Todos os status</option>
          <option value="ABERTA">Aberta</option>
          <option value="ANDAMENTO">Em andamento</option>
          <option value="CONCLUIDA">Concluída</option>
          <option value="CANCELADA">Cancelada</option>
        </select>
      </div>

      <div class="campo-filtro">
        <label for="prioridade">Prioridade</label>
        <select id="prioridade" v-model="filtroPrioridade">
          <option value="">Todas as prioridades</option>
          <option value="BAIXA">Baixa</option>
          <option value="MEDIA">Média</option>
          <option value="ALTA">Alta</option>
          <option value="CRITICA">Crítica</option>
        </select>
      </div>
    </div>

    <div class="resumo-lista">
      <span>
        Total de ordens: <strong>{{ ordensFiltradas.length }}</strong>
      </span>

      <span v-if="ordemStore.loading" class="status-carregando">
        Carregando ordens...
      </span>

      <span v-if="ordemStore.error" class="status-erro">
        {{ ordemStore.error }}
      </span>
    </div>

    <div class="card-tabela">
      <table v-if="ordensFiltradas.length > 0">
        <thead>
          <tr>
            <th>Título</th>
            <th>Máquina</th>
            <th>Setor</th>
            <th>Técnico</th>
            <th>Prioridade</th>
            <th>Status</th>
            <th class="coluna-acoes">Ações</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="ordem in ordensFiltradas" :key="ordem.idOrdem">
            <td>{{ ordem.titulo }}</td>

            <td>
              {{ formatarMaquina(ordem) }}
            </td>

            <td>{{ ordem.nomeSetor || "Sem setor" }}</td>

            <td>{{ ordem.nomeTecnico || "Não atribuído" }}</td>

            <td>
              <span
                class="badge-prioridade"
                :class="classePrioridade(ordem.prioridade)"
              >
                {{ formatarPrioridade(ordem.prioridade) }}
              </span>
            </td>

            <td>
              <span class="badge-status" :class="classeStatus(ordem.status)">
                {{ formatarStatus(ordem.status) }}
              </span>
            </td>

            <td class="acoes">
              <button class="btn-detalhes" @click="abrirDetalhes(ordem)">
                Detalhes
              </button>

              <button class="btn-editar" @click="editarOrdem(ordem.idOrdem)">
                Editar
              </button>

              <button
                v-if="ordem.status !== 'CANCELADA'"
                class="btn-cancelar-ordem"
                @click="cancelarOrdem(ordem.idOrdem)"
              >
                Cancelar
              </button>

              <span v-else class="texto-cancelada"> Já cancelada </span>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-else class="sem-registros">Nenhuma ordem encontrada.</div>
    </div>

    <div v-if="ordemSelecionada" class="modal-fundo">
      <div class="modal-card">
        <div class="modal-cabecalho">
          <div>
            <h2>{{ ordemSelecionada.titulo }}</h2>
            <p>Detalhes da ordem de manutenção</p>
          </div>

          <button class="btn-fechar" @click="fecharDetalhes">Fechar</button>
        </div>

        <div class="modal-conteudo">
          <p>
            <strong>Descrição:</strong>
            {{ ordemSelecionada.descricao || "Sem descrição." }}
          </p>

          <p>
            <strong>Máquina:</strong>
            {{ formatarMaquina(ordemSelecionada) }}
          </p>

          <p>
            <strong>Setor:</strong>
            {{ ordemSelecionada.nomeSetor || "Sem setor" }}
          </p>

          <p>
            <strong>Técnico:</strong>
            {{ ordemSelecionada.nomeTecnico || "Não atribuído" }}
          </p>

          <p>
            <strong>Prioridade:</strong>
            {{ formatarPrioridade(ordemSelecionada.prioridade) }}
          </p>

          <p>
            <strong>Status:</strong>
            {{ formatarStatus(ordemSelecionada.status) }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useOrdensStore } from "@/stores/ordemStore";

const router = useRouter();
const ordemStore = useOrdensStore();

const pesquisa = ref("");
const filtroStatus = ref("");
const filtroPrioridade = ref("");
const ordemSelecionada = ref(null);

onMounted(async () => {
  await ordemStore.buscarOrdens();
});

const ordensFiltradas = computed(() => {
  const termoPesquisa = pesquisa.value.toLowerCase().trim();

  return ordemStore.ordens.filter((ordem) => {
    const maquina = formatarMaquina(ordem).toLowerCase();

    const correspondePesquisa =
      !termoPesquisa ||
      ordem.titulo?.toLowerCase().includes(termoPesquisa) ||
      ordem.descricao?.toLowerCase().includes(termoPesquisa) ||
      maquina.includes(termoPesquisa) ||
      ordem.nomeSetor?.toLowerCase().includes(termoPesquisa) ||
      ordem.nomeTecnico?.toLowerCase().includes(termoPesquisa);

    const correspondeStatus =
      !filtroStatus.value || ordem.status === filtroStatus.value;

    const correspondePrioridade =
      !filtroPrioridade.value || ordem.prioridade === filtroPrioridade.value;

    return correspondePesquisa && correspondeStatus && correspondePrioridade;
  });
});

function cadastrarOrdem() {
  router.push("/ordens/cadastro");
}

function editarOrdem(id) {
  router.push(`/ordens/editar/${id}`);
}

async function cancelarOrdem(id) {
  const confirmar = confirm("Deseja realmente cancelar esta ordem?");

  if (!confirmar) {
    return;
  }

  try {
    await ordemStore.cancelarOrdem(id);
    await ordemStore.buscarOrdens();
  } catch (erro) {
    alert("Não foi possível cancelar a ordem.");
  }
}

function abrirDetalhes(ordem) {
  ordemSelecionada.value = ordem;
}

function fecharDetalhes() {
  ordemSelecionada.value = null;
}

function formatarMaquina(ordem) {
  const codigo = ordem.codigoIdentificador || "";
  const modelo = ordem.modelo || "";

  if (codigo && modelo) {
    return `${codigo} - ${modelo}`;
  }

  if (codigo) {
    return codigo;
  }

  if (modelo) {
    return modelo;
  }

  return "Máquina não informada";
}

function formatarStatus(status) {
  const statusMap = {
    ABERTA: "Aberta",
    ANDAMENTO: "Em andamento",
    CONCLUIDA: "Concluída",
    CANCELADA: "Cancelada",
  };

  return statusMap[status] || status;
}

function formatarPrioridade(prioridade) {
  const prioridadeMap = {
    BAIXA: "Baixa",
    MEDIA: "Média",
    ALTA: "Alta",
    CRITICA: "Crítica",
  };

  return prioridadeMap[prioridade] || prioridade;
}

function classeStatus(status) {
  return {
    aberta: status === "ABERTA",
    andamento: status === "ANDAMENTO",
    concluida: status === "CONCLUIDA",
    cancelada: status === "CANCELADA",
  };
}

function classePrioridade(prioridade) {
  return {
    baixa: prioridade === "BAIXA",
    media: prioridade === "MEDIA",
    alta: prioridade === "ALTA",
    critica: prioridade === "CRITICA",
  };
}
</script>

<style scoped>
.pagina-ordens {
  width: 100%;
}

.cabecalho-pagina {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 24px;
}

.cabecalho-pagina h1 {
  font-size: 30px;
  color: #1e3a8a;
  margin-bottom: 6px;
}

.cabecalho-pagina p {
  color: #64748b;
  font-size: 15px;
}

.btn-cadastrar {
  border: none;
  background: #1e3a8a;
  color: #ffffff;
  padding: 12px 18px;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
}

.btn-cadastrar:hover {
  background: #1d4ed8;
}

.card-filtros {
  background: #ffffff;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 18px;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.08);
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: 16px;
}

.campo-pesquisa,
.campo-filtro {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.campo-pesquisa label,
.campo-filtro label {
  font-weight: 600;
  color: #334155;
  font-size: 14px;
}

.campo-pesquisa input,
.campo-filtro select {
  width: 100%;
  padding: 11px 13px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  font-size: 14px;
  outline: none;
  background: #ffffff;
}

.campo-pesquisa input:focus,
.campo-filtro select:focus {
  border-color: #2563eb;
}

.resumo-lista {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  color: #475569;
  font-size: 14px;
}

.status-carregando {
  color: #2563eb;
  font-weight: 600;
}

.status-erro {
  color: #dc2626;
  font-weight: 600;
}

.card-tabela {
  background: #ffffff;
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.08);
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: #f1f5f9;
}

th,
td {
  padding: 14px 12px;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
  color: #334155;
  font-size: 14px;
}

th {
  font-weight: 700;
  color: #1e293b;
}

.coluna-acoes {
  text-align: center;
}

.acoes {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.btn-detalhes,
.btn-editar,
.btn-cancelar-ordem {
  border: none;
  padding: 8px 11px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
}

.btn-detalhes,
.btn-editar {
  background: #dbeafe;
  color: #1d4ed8;
}

.btn-cancelar-ordem {
  background: #fee2e2;
  color: #b91c1c;
}

.btn-detalhes:hover,
.btn-editar:hover {
  background: #bfdbfe;
}

.btn-cancelar-ordem:hover {
  background: #fecaca;
}

.badge-status,
.badge-prioridade {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
}

.aberta {
  background: #f1f5f9;
  color: #334155;
}

.andamento {
  background: #dbeafe;
  color: #1d4ed8;
}

.concluida {
  background: #dcfce7;
  color: #166534;
}

.cancelada {
  background: #e5e7eb;
  color: #4b5563;
}

.baixa {
  background: #dcfce7;
  color: #166534;
}

.media {
  background: #fef3c7;
  color: #92400e;
}

.alta {
  background: #ffedd5;
  color: #c2410c;
}

.critica {
  background: #fee2e2;
  color: #b91c1c;
}

.texto-cancelada {
  color: #64748b;
  font-size: 13px;
  font-weight: 600;
}

.sem-registros {
  padding: 28px;
  text-align: center;
  color: #64748b;
  font-size: 15px;
}

.modal-fundo {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.45);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 24px;
  z-index: 20;
}

.modal-card {
  width: 100%;
  max-width: 620px;
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 20px 45px rgba(15, 23, 42, 0.2);
}

.modal-cabecalho {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 20px;
}

.modal-cabecalho h2 {
  color: #1e3a8a;
  font-size: 24px;
  margin-bottom: 4px;
}

.modal-cabecalho p {
  color: #64748b;
  font-size: 14px;
}

.modal-conteudo {
  display: flex;
  flex-direction: column;
  gap: 12px;
  color: #334155;
  font-size: 15px;
}

.btn-fechar {
  border: none;
  border-radius: 10px;
  padding: 10px 14px;
  background: #e2e8f0;
  color: #334155;
  font-weight: 600;
  cursor: pointer;
}

.btn-fechar:hover {
  background: #cbd5e1;
}

@media (max-width: 900px) {
  .cabecalho-pagina {
    flex-direction: column;
  }

  .card-filtros {
    grid-template-columns: 1fr;
  }

  .btn-cadastrar {
    width: 100%;
  }

  .resumo-lista {
    flex-direction: column;
    align-items: flex-start;
  }

  .acoes {
    flex-direction: column;
  }
}
</style>
