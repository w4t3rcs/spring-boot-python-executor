# Security Policy for spring-boot-python-executor

## Summary
This repository (spring-boot-python-executor) takes security seriously. This document describes how we handle vulnerability reports, what versions we support, and our disclosure policy. If you discover a potential security issue, please follow the instructions below to ensure a timely, coordinated, and responsible resolution.

---

## Security Policy

| Major release              | Supported for security fixes                                                                     |
|----------------------------|--------------------------------------------------------------------------------------------------|
| All active major releases  | **Only the latest minor version** of each supported major release will receive security updates. |
| End-of-life major releases | Not supported — no security updates will be provided.                                            |

**Notes**
- “Latest minor version” means the most recent `x.y.z` published for a given major `x` at the time of the report. Consumers are encouraged to run the latest minor release within each major line to receive security fixes.
- If you are unsure whether a particular version is supported, contact the security email below.

---

## Reporting a Vulnerability

**Preferred channels (private):**
1. Email: **w4t3rofficial@gmail.com** — use this for private reports if you prefer direct, private communication.  
2. GitHub Security Advisories for this repository — open a private security advisory at:  
   `https://github.com/w4t3rcs/spring-boot-python-executor/security/advisories`

**Do not** create a public GitHub issue describing the vulnerability. Public disclosure before a fix is released may put users at risk.

### What to include in your report
Please provide as much of the following information as you can to help us triage and reproduce the issue:

- **Short summary** (one or two sentences) describing the issue and impact.
- **Component / module** affected (e.g., `spring-boot-python-executor-core`, REST executor, gRPC executor, demo server).
- **Versions** affected (artifact coordinates or Git commit/tag). If you cannot determine exact versions, provide a `pom.xml` snippet or the output of `mvn dependency:tree`.
- **Environment** where the problem occurs (Java version, Spring Boot version, Python version, OS).
- **Steps to reproduce** — minimal, copy-and-paste commands or code snippets that demonstrate the issue.
- **Expected vs actual behavior**.
- **Proof-of-concept** (PoC) or exploit code if available — share as a private attachment or inlined code that we can execute locally.
- **Logs, stack traces, configuration files** (masked for secrets where appropriate).
- **Any mitigation or workaround** you believe is relevant.

We treat sensitive attachments as confidential. If you need to send large files, ask first, and we will provide guidance.

---

## Disclosure Policy

**Acknowledgement**
- We will acknowledge receipt of your report within **72 hours** of receiving it via the channel you used.

**Triage and response**
- Initial triage and severity classification will be completed within **7 calendar days**.
- For confirmed issues, we will:
  - Assign a severity level and create an internal ticket.
  - Work on a fix or mitigation plan. For high- and critical-severity issues, we will prioritize immediate mitigation.
  - Where necessary, coordinate with downstream consumers and the maintainers of related projects.

**Fix and release timeline**
- For **critical** vulnerabilities that enable remote code execution, data exfiltration, or other severe impacts, we will aim to provide a patch or mitigation **as quickly as feasible**, typically within **14 days**, and publish a security release as soon as the fix has been validated.
- For **high** severity issues, we aim for a fix within **30 days**.
- For **medium/low** severity issues, fixes will be scheduled according to maintenance cycles, but we will endeavor to address them in a timely manner.
- If a longer remediation window is required (for example, because a fix risks breaking backward compatibility), we will communicate a mitigation plan and a public timeline to the reporter and coordinate on disclosure.

**Coordinated disclosure**
- We prefer coordinated disclosure: we will **not** disclose the vulnerability publicly until:
  1. a fix or mitigation is available (or an agreed workaround is published), and
  2. any required advisories and release notes are prepared.
- We will coordinate with the reporter on the timing of the public disclosure and crediting. If you prefer to remain anonymous, tell us in the report, and we will respect that preference.

---

## After a fix is available

- A patched release will be published on the project’s release channel and to Maven Central when applicable.
- Release notes will include a high-level description of the issue and acknowledgement of the reporter (if they consent).
- We will provide guidance for upgrading and any migration steps required.

---

## Handling sensitive data
- Do **not** include credentials, API keys, or personal data in public reports. Use the private reporting channels above. If you accidentally include secrets in a public issue, notify us immediately via email so we can help mitigate exposure.

---

## Security best practices for users
- Run the latest minor release of the major versions you depend on.
- If executing untrusted Python code, follow the library’s sandboxing recommendations and use RestrictedPython mode or isolated execution environments (containers).
- Keep your platform dependencies (Java, Spring Boot, Python) up to date with security patches.

---

## Legal and responsible disclosure notes
- We encourage responsible disclosure and will not pursue legal action against security researchers acting in good faith who follow this policy.
- Do not attempt to access or modify data belonging to users other than your own as part of testing.

---

## Credits
We appreciate the time and effort of security researchers who help improve this project. Reporters who help with responsible disclosure and permit acknowledgement will be credited in release notes unless they request anonymity.

---

Thank you for helping keep `spring-boot-python-executor` secure.
