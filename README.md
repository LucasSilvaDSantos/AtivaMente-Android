#  AtivaMente — Gestão de Tarefas e Rotinas

Aplicativo Android nativo focado em produtividade pessoal, organização de rotinas diárias e acompanhamento de progreção, com funcionamento offline e notificações.

---

## 📖 Sobre o Projeto

O **AtivaMente** foi desenvolvido com foco em ajudar usuários a estruturarem suas rotinas diárias com lembretes pontuais.
* Arquitetura MVVM (Model-View-ViewModel)
* Persistência Local com Room Database (SQLite)
* Agendamento de tarefas em segundo plano
* Autenticação e nuvem com Firebase
* UI declarativa com Material Design 3 e alternância de temas (Light/Dark Mode)

---

## 🚀 Funcionalidades

O aplicativo oferece uma experiência de organização diária:

* 📝 **Gestão de Tarefas e Rotinas:** Criação, edição e exclusão de tarefas diárias ou de rotinas (com seleção dos dias da semana).
* 🔔 ** Notificações Precisas:** Agendamento em segundo plano que dispara lembretes com hora marcada na barra de notificações.
* 📊 **Explore e % de Desempenho:** Acompanhamento de metas diárias em porcentagem , sequência de dias e tarefas concluídas.
* 🌗 **Temas:** Alternância entre Modo Claro e Modo Escuro.
* 🔒 **Autenticação Segura:** Cadastro de conta, login e logout gerenciados via Firebase Authentication.
* 📅 **Calendário Dinâmico:** Navegação entre os dias da semana para visualização isolada de cada dia.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** 100 % em Java (Android SDK)
* **Design & UI:** XML Declarativo com Material Design 3 (Material)
* **Arquitetura:** MVVM
* **Persistência de Dados:** Room Database (SQLite)
* **Serviços de Background:** BroadcastReceiver e NotificationManager
* **Segurança:** Firebase Authentication
* **Build System:** Gradle (Kotlin DSL - `.kts`)
* **IDE:** Android Studio
* **Controle de Versão:** Git & GitHub

---

## 📂 Estrutura do Projeto

O código foi organizado seguindo padrões e separação de responsabilidades:

```text
com.example.ativamente/
├── dao/            # Interfaces de acesso ao banco de dados (TaskDao)
├── database/       # Configuração e instância do Room Database (AppDatabase)
├── model/          # Entidades e classes modelo de dados (Task, User)
├── receiver/       # BroadcastReceivers para escuta de alarmes do sistema (Futura)
├── repository/     # Camada de abstração e intermediação de dados
├── view/           # Componentes visuais de UI
│   ├── adapter/    # Adaptadores para Listas e Calendário
│   ├── fragment/   # Fragmentos das telas (Home, Explore, Perfil, Login, etc...)
│   └── dialog/     # Caixas de diálogo
├── viewmodel/      # Camada de gerenciamento de estado e regras de UI
├── MainActivity    # Activity com barra de navegação
└── SplashActivity  # Tela inicial de abertura e verificação de sessão
```

<p align="center">
  <img src="https://github.com/user-attachments/assets/63266bcd-5673-4211-aee6-ecc0b29a5af8" width="30%" />
  <img src="https://github.com/user-attachments/assets/af01ee95-4dc5-4cb6-9711-ce83da551d24" width="30%" />
  <img src="https://github.com/user-attachments/assets/7b46e3ca-379f-4780-8621-50d4b1adf578" width="30%" />
  <img src="https://github.com/user-attachments/assets/10fdcbae-e674-4040-ac07-698204e64b4c" width="30%" />
  <img src="https://github.com/user-attachments/assets/ffb6f606-0e7f-45eb-96fc-25da1e13653f" width="30%" />
  <img src="https://github.com/user-attachments/assets/ec8bd7bd-cff4-4b95-8e79-ebba0f9d7209" width="30%" />
  <img src="https://github.com/user-attachments/assets/9445bc62-131b-4383-a6cc-00e82910fcdb" width="30%" />
</p>

