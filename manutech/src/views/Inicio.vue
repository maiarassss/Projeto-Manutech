<script setup>
import { computed, onMounted } from "vue";
import { RouterLink } from "vue-router";
import {
  Wrench,
  Cpu,
  Users,
  AlertTriangle,
  CheckCircle,
  PlusCircle,
  ClipboardList,
} from "lucide-vue-next";

import { useMaquinasStore } from "@/stores/maquinaStore";
import { useTecnicosStore } from "@/stores/tecnicoStore";
import { useOrdensStore } from "@/stores/ordemStore";
import { useAuthStore } from "@/stores/authStore";

const maquinaStore = useMaquinasStore();
const tecnicoStore = useTecnicosStore();
const ordemStore = useOrdensStore();
const authStore = useAuthStore();

onMounted(() => {
  maquinaStore.buscarMaquinas();
  tecnicoStore.buscarTecnicos();
  ordemStore.buscarOrdens();
});

const totalMaquinas = computed(() => maquinaStore.maquinas.length);
const maquinasAtivas = computed(
  () => maquinaStore.maquinas.filter((maquina) => maquina.ativa).length,
);

const totalTecnicos = computed(() => tecnicoStore.tecnicos.length);

const totalOrdens = computed(() => ordemStore.ordens.length);

const ordensEmAndamento = computed(
  () =>
    ordemStore.ordens.filter((ordem) => ordem.status === "ANDAMENTO").length,
);

const ordensCriticas = computed(
  () =>
    ordemStore.ordens.filter((ordem) => ordem.prioridade === "CRITICA").length,
);

const ordensCanceladas = computed(
  () =>
    ordemStore.ordens.filter((ordem) => ordem.status === "CANCELADA").length,
);
</script>

<template>
  <section class="inicio">
    <div class="hero">
      <div>
        <p class="subtitulo">Gestão de manutenção industrial</p>

        <h1>Bem-vindo ao Manutech</h1>

        <p class="descricao">
          Acompanhe ordens de serviço, consulte máquinas cadastradas e organize
          os responsáveis pelos atendimentos de manutenção.
        </p>

        <div class="acoes-hero">
          <RouterLink
            v-if="authStore.ehGestor"
            to="/ordens/cadastro"
            class="botao-principal"
          >
            <PlusCircle :size="20" />
            Cadastrar ordem
          </RouterLink>

          <RouterLink to="/ordens" class="botao-secundario">
            Ver ordens
          </RouterLink>
        </div>
      </div>

      <div class="card-destaque">
        <Wrench :size="42" />

        <div>
          <span>{{ ordensEmAndamento }}</span>
          <p>ordens em andamento</p>
        </div>
      </div>
    </div>

    <div class="cards-resumo">
      <div class="card">
        <div class="icone">
          <Cpu :size="28" />
        </div>

        <div>
          <h2>{{ totalMaquinas }}</h2>
          <p>Máquinas cadastradas</p>
          <small>{{ maquinasAtivas }} máquinas ativas</small>
        </div>
      </div>

      <div class="card">
        <div class="icone">
          <Users :size="28" />
        </div>

        <div>
          <h2>{{ totalTecnicos }}</h2>
          <p>Técnicos cadastrados</p>
          <small>Equipe responsável pelos atendimentos</small>
        </div>
      </div>

      <div class="card">
        <div class="icone">
          <Wrench :size="28" />
        </div>

        <div>
          <h2>{{ totalOrdens }}</h2>
          <p>Ordens registradas</p>
          <small>{{ ordensEmAndamento }} em andamento</small>
        </div>
      </div>

      <div class="card alerta">
        <div class="icone">
          <AlertTriangle :size="28" />
        </div>

        <div>
          <h2>{{ ordensCriticas }}</h2>
          <p>Ordens críticas</p>
          <small>{{ ordensCanceladas }} ordens canceladas</small>
        </div>
      </div>
    </div>

    <div class="area-inferior">
      <div class="painel">
        <h2>Acessos rápidos</h2>

        <div class="atalhos">
          <RouterLink v-if="authStore.ehGestor" to="/maquinas" class="atalho">
            <Cpu :size="22" />
            Gerenciar máquinas
          </RouterLink>

          <RouterLink v-if="authStore.ehGestor" to="/tecnicos" class="atalho">
            <Users :size="22" />
            Gerenciar técnicos
          </RouterLink>

          <RouterLink to="/ordens" class="atalho">
            <Wrench :size="22" />
            Gerenciar ordens
          </RouterLink>
        </div>
      </div>

      <div class="painel status-sistema">
        <h2>Fluxo de manutenção</h2>

        <div class="linha-status">
          <CheckCircle :size="22" />
          <span
            >Registre solicitações de manutenção para as máquinas
            cadastradas.</span
          >
        </div>

        <div class="linha-status">
          <ClipboardList :size="22" />
          <span
            >Acompanhe o andamento das ordens abertas, em execução ou
            concluídas.</span
          >
        </div>

        <div class="linha-status">
          <CheckCircle :size="22" />
          <span
            >Mantenha o histórico de atendimentos organizado por máquina e
            responsável.</span
          >
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.inicio {
  width: 100%;
}

.hero {
  background: linear-gradient(135deg, #144e94, #1e293b);

  color: white;

  border-radius: 18px;

  padding: 45px;

  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 30px;

  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.subtitulo {
  margin: 0 0 10px;

  text-transform: uppercase;
  letter-spacing: 1px;

  font-size: 0.8rem;
  font-weight: bold;

  color: #cbd5e1;
}

.hero h1 {
  margin: 0;

  font-size: 3rem;
  color: white;
}

.descricao {
  max-width: 620px;

  margin: 15px 0 30px;

  font-size: 1.1rem;
  line-height: 1.5;

  color: #e2e8f0;
}

.acoes-hero {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.botao-principal,
.botao-secundario {
  min-height: 48px;

  padding: 0 22px;

  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;

  border-radius: 10px;

  text-decoration: none;
  font-weight: bold;

  transition: 0.2s;
}

.botao-principal {
  background: white;
  color: #144e94;
}

.botao-principal:hover {
  background: #e2e8f0;
}

.botao-secundario {
  border: 1px solid white;
  color: white;
}

.botao-secundario:hover {
  background: rgba(255, 255, 255, 0.15);
}

.card-destaque {
  min-width: 260px;

  background: rgba(255, 255, 255, 0.12);

  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;

  padding: 25px;

  display: flex;
  align-items: center;
  gap: 18px;
}

.card-destaque span {
  font-size: 2.6rem;
  font-weight: bold;
}

.card-destaque p {
  margin: 0;
  color: #e2e8f0;
}

.cards-resumo {
  margin-top: 30px;

  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.card {
  background: white;

  padding: 24px;

  border-radius: 16px;

  display: flex;
  align-items: center;
  gap: 18px;

  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.07);
}

.icone {
  width: 56px;
  height: 56px;

  background: #e8f0fe;
  color: #144e94;

  border-radius: 14px;

  display: flex;
  align-items: center;
  justify-content: center;

  flex-shrink: 0;
}

.card.alerta .icone {
  background: #fff1c2;
  color: #8a6d00;
}

.card h2 {
  margin: 0;

  font-size: 2rem;
  color: #1e293b;
}

.card p {
  margin: 4px 0;

  font-weight: bold;
  color: #334155;
}

.card small {
  color: #64748b;
}

.area-inferior {
  margin-top: 30px;

  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.painel {
  background: white;

  padding: 28px;

  border-radius: 16px;

  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.07);
}

.painel h2 {
  margin-top: 0;
  margin-bottom: 20px;

  color: #1e293b;
}

.atalhos {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.atalho {
  padding: 16px;

  background: #f8fafc;

  border: 1px solid #e2e8f0;
  border-radius: 12px;

  color: #144e94;
  text-decoration: none;
  font-weight: bold;

  display: flex;
  align-items: center;
  gap: 12px;

  transition: 0.2s;
}

.atalho:hover {
  background: #e8f0fe;
  border-color: #144e94;
}

.status-sistema {
  display: flex;
  flex-direction: column;
}

.linha-status {
  display: flex;
  align-items: center;
  gap: 12px;

  padding: 14px 0;

  color: #334155;

  border-bottom: 1px solid #e2e8f0;
}

.linha-status:last-child {
  border-bottom: none;
}

.linha-status svg {
  color: #1d7d38;
  flex-shrink: 0;
}

@media (max-width: 1100px) {
  .cards-resumo {
    grid-template-columns: repeat(2, 1fr);
  }

  .hero {
    flex-direction: column;
    align-items: flex-start;
  }

  .card-destaque {
    width: 100%;
  }
}

@media (max-width: 760px) {
  .cards-resumo,
  .area-inferior {
    grid-template-columns: 1fr;
  }

  .hero h1 {
    font-size: 2.2rem;
  }

  .hero {
    padding: 30px;
  }
}
</style>
