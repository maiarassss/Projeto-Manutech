<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { Lock, UserCircle } from "lucide-vue-next";

const router = useRouter();

const nome = ref("");
const senha = ref("");
const erro = ref("");

function entrar() {
  erro.value = "";

  if (!nome.value.trim()) {
    erro.value = "Informe o nome de usuário.";
    return;
  }

  if (!senha.value.trim()) {
    erro.value = "Informe a senha.";
    return;
  }

  router.push("/");
}
</script>

<template>
  <main class="login-page">
    <section class="login-card">
      <div class="login-header">
        <div class="icone-login">
          <UserCircle :size="54" />
        </div>

        <h1>Manutech</h1>

        <p>Acesse o sistema de gerenciamento de manutenção.</p>
      </div>

      <form class="form-login">
        <p v-if="erro" class="mensagem-erro">
          {{ erro }}
        </p>

        <label>
          Nome de usuário
          <input
            v-model="nome"
            type="text"
            placeholder="Digite seu nome de usuário"
          />
        </label>

        <label>
          Senha
          <input
            v-model="senha"
            type="password"
            placeholder="Digite sua senha"
          />
        </label>

        <button type="button" @click="entrar">
          <Lock :size="20" />
          Entrar
        </button>
      </form>

      <p class="aviso">
        Login visual antecipado para futura integração com Spring Security.
      </p>
    </section>
  </main>
</template>

<style scoped>
.login-page {
  width: 100vw;
  min-height: 100vh;

  background: linear-gradient(135deg, #144e94, #1e293b);

  display: flex;
  align-items: center;
  justify-content: center;

  padding: 30px;
}

.login-card {
  width: 100%;
  max-width: 430px;

  background: white;

  padding: 38px;

  border-radius: 18px;

  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.25);
}

.login-header {
  text-align: center;
  margin-bottom: 28px;
}

.icone-login {
  width: 82px;
  height: 82px;

  margin: 0 auto 16px;

  background: #e8f0fe;
  color: #144e94;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;
}

.login-header h1 {
  margin: 0;

  color: #144e94;
  font-size: 2.4rem;
}

.login-header p {
  margin-top: 8px;

  color: #64748b;
  font-size: 1rem;
}

.form-login {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 7px;

  color: #1e293b;
  font-weight: bold;
}

input {
  height: 48px;

  padding: 0 14px;

  border: 1px solid #cbd5e1;
  border-radius: 10px;

  font-size: 1rem;
  outline: none;
}

input:focus {
  border-color: #144e94;
}

button {
  height: 50px;

  background: #144e94;
  color: white;

  border: none;
  border-radius: 10px;

  font-weight: bold;
  font-size: 1rem;

  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;

  cursor: pointer;

  transition: 0.2s;
}

button:hover {
  background: #0f3f78;
}

.mensagem-erro {
  background: #ffd9d9;
  color: #b42318;

  padding: 12px;

  border-radius: 8px;

  font-weight: bold;
  text-align: center;
}

.aviso {
  margin-top: 22px;

  color: #64748b;
  font-size: 0.85rem;
  text-align: center;
}
</style>
