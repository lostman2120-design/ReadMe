import { WaitlistForm } from "@/components/WaitlistForm";

export default function Home() {
  return (
    <main className="min-h-screen bg-paper text-ink">
      <Nav />
      <Hero />
      <Problem />
      <Solution />
      <Comparison />
      <WhoFor />
      <HowItWorks />
      <Privacy />
      <Pricing />
      <FAQ />
      <FinalCTA />
      <Footer />
    </main>
  );
}

function Nav() {
  return (
    <header className="container-prose flex items-center justify-between py-6">
      <a href="#top" className="text-lg font-semibold tracking-tight">
        Replyy<span className="text-accent">.</span>
      </a>
      <nav className="hidden gap-8 text-sm text-ink-muted sm:flex">
        <a href="#problem" className="hover:text-ink">Problem</a>
        <a href="#how" className="hover:text-ink">How it works</a>
        <a href="#pricing" className="hover:text-ink">Pricing</a>
        <a href="#faq" className="hover:text-ink">FAQ</a>
      </nav>
      <a href="#cta" className="btn-ghost px-4 py-2 text-sm">Join waitlist</a>
    </header>
  );
}

function Hero() {
  return (
    <section id="top" className="container-prose pt-12 pb-24 sm:pt-20 sm:pb-32">
      <p className="eyebrow mb-6">Launching June 2026 · 47 founding spots left</p>
      <h1 className="font-serif text-5xl leading-[1.05] tracking-tight sm:text-6xl md:text-7xl">
        Reply with scheduling.
        <br />
        <span className="text-ink-muted">Not a Calendly link.</span>
      </h1>
      <p className="mt-8 max-w-prose text-lg leading-relaxed text-ink-soft sm:text-xl">
        Paste the email you got. Replyy drafts three personal-sounding replies —
        each with real times pulled from your calendar — so you can close{" "}
        <strong className="text-ink">$50k+ deals</strong> without sounding like a
        SaaS form.
      </p>
      <div className="mt-10 max-w-xl">
        <WaitlistForm />
        <p className="mt-3 text-sm text-ink-muted">
          Founding members lock in <strong className="text-ink">$9/mo</strong> for
          life. No charge today.
        </p>
      </div>
      <p className="mt-12 max-w-prose text-sm text-ink-muted">
        Built for executive sales, premium consulting, exec coaches, and anyone
        whose prospects don&rsquo;t click links.
      </p>
    </section>
  );
}

function Problem() {
  return (
    <section id="problem" className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">The problem</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Your CRM says &ldquo;send a Calendly link.&rdquo;
          <br />
          <span className="text-ink-muted">Your prospect says &ldquo;I&rsquo;ll think about it.&rdquo;</span>
        </h2>
        <div className="mt-10 max-w-prose space-y-6 text-lg leading-relaxed text-ink-soft">
          <p>
            For high-trust deals, scheduling links are a tell. They say{" "}
            <em>I&rsquo;m batching you</em>. They say <em>I&rsquo;m one of forty
            people in your pipeline today</em>. Your prospect — a CEO, a GC, a
            managing partner — knows the move. They quietly downgrade you from{" "}
            <em>potential partner</em> to <em>vendor</em>.
          </p>
          <p>
            So you write the email by hand. You check your calendar. You propose
            three times. You hedge in case they prefer next week. You re-read it.
            You send it at 9:14 AM. You wait.
          </p>
          <p>
            You do this{" "}
            <strong className="text-ink">forty-seven times a month</strong>.
            That&rsquo;s three full workdays of typing the same six sentences.
          </p>
        </div>
      </div>
    </section>
  );
}

function Solution() {
  const steps = [
    {
      n: "01",
      title: "Paste their email",
      body:
        "Drop in the message you just got. Replyy reads the tone — formal, warm, urgent, hedging — and the meeting context.",
    },
    {
      n: "02",
      title: "Pick a reply",
      body:
        "Get three drafts in your voice. Each pulls live availability from your calendar. Each sounds like you wrote it on a quiet Tuesday morning.",
    },
    {
      n: "03",
      title: "Send from your inbox",
      body:
        "Copy. Paste. Hit send. We never touch your outbox. Your prospect sees a normal email from you — because that's what it is.",
    },
  ];
  return (
    <section className="border-t border-ink/10 py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">The solution</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Paste. Pick. Send.
        </h2>
        <div className="mt-12 grid gap-6 sm:grid-cols-3">
          {steps.map((s) => (
            <div
              key={s.n}
              className="rounded-2xl border border-ink/10 bg-white p-6"
            >
              <p className="font-serif text-2xl text-accent">{s.n}</p>
              <h3 className="mt-3 text-lg font-semibold">{s.title}</h3>
              <p className="mt-2 text-base leading-relaxed text-ink-soft">
                {s.body}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function Comparison() {
  const rows: Array<[string, string, string]> = [
    ["Feels like", "A booking form", "A personal reply"],
    [
      "Best for",
      "SMB sales, demos, recruiters",
      "Enterprise sales, exec coaching, legal, advisory",
    ],
    [
      "Prospect effort",
      "Click, scroll, pick, confirm",
      "Read one sentence, reply “Tuesday 2pm works”",
    ],
    [
      "Signal to prospect",
      "You’re one of many",
      "I made time for you",
    ],
    [
      "Calendar integration",
      "Yes",
      "Yes — same Google/Outlook backend",
    ],
    ["Price", "$12–$20/mo", "$9/mo founding price"],
  ];
  return (
    <section className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">Why not Calendly</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Calendly is great.
          <br />
          <span className="text-ink-muted">
            For people who don&rsquo;t care if you click a link.
          </span>
        </h2>
        <div className="mt-12 overflow-hidden rounded-2xl border border-ink/10">
          <table className="w-full text-left">
            <thead className="bg-ink text-paper">
              <tr>
                <th className="px-6 py-4 text-sm font-medium"></th>
                <th className="px-6 py-4 text-sm font-medium">Calendly</th>
                <th className="px-6 py-4 text-sm font-medium">Replyy</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-ink/10 bg-paper">
              {rows.map(([label, a, b]) => (
                <tr key={label}>
                  <td className="px-6 py-4 text-sm font-medium text-ink-muted">
                    {label}
                  </td>
                  <td className="px-6 py-4 text-base">{a}</td>
                  <td className="px-6 py-4 text-base font-medium">{b}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
}

function WhoFor() {
  const cards = [
    {
      title: "Enterprise AEs",
      body: "Closing six-figure deals with VP+ buyers who don’t click links.",
    },
    {
      title: "Executive coaches",
      body: "Booking C-suite discovery calls where the first email is the pitch.",
    },
    {
      title: "Boutique consultants",
      body: "Where the reply tone signals the quality of the engagement.",
    },
    {
      title: "Lawyers & advisors",
      body: "Whose hourly rate makes Calendly feel beneath the brand.",
    },
  ];
  return (
    <section className="border-t border-ink/10 py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">Who it&rsquo;s for</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          If your average deal is over $25k,
          <br />
          <span className="text-ink-muted">Replyy was built for you.</span>
        </h2>
        <div className="mt-12 grid gap-6 sm:grid-cols-2">
          {cards.map((c) => (
            <div
              key={c.title}
              className="rounded-2xl border border-ink/10 bg-white p-6"
            >
              <h3 className="text-lg font-semibold">{c.title}</h3>
              <p className="mt-2 text-base leading-relaxed text-ink-soft">
                {c.body}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function HowItWorks() {
  return (
    <section id="how" className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">How the AI sounds like you</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          It learns from five emails.
          <br />
          <span className="text-ink-muted">Then it disappears.</span>
        </h2>
        <div className="mt-10 max-w-prose space-y-6 text-lg leading-relaxed text-ink-soft">
          <p>
            Onboarding is forty seconds. Forward us five emails you&rsquo;ve sent.
            We extract your sentence length, your sign-offs, your hedges, your
            warmth. Then we draft in that voice — no em-dashes you&rsquo;d never
            use, no &ldquo;I hope this finds you well&rdquo; if you&rsquo;d never
            write it.
          </p>
          <p>
            You stay in control. You pick from three drafts. You edit anything.
            We don&rsquo;t auto-send. We don&rsquo;t track opens. We don&rsquo;t
            put a tracking pixel in your email.{" "}
            <strong className="text-ink">
              The product is the draft, not the surveillance.
            </strong>
          </p>
        </div>
      </div>
    </section>
  );
}

function Privacy() {
  const items = [
    {
      title: "Read-only calendar access",
      body: "We see your busy blocks, never event titles or attendees.",
    },
    {
      title: "No email sending",
      body: "You paste in, we draft out. Your SMTP is untouched.",
    },
    {
      title: "Drafts deleted after 30 days",
      body: "Or instantly, from your dashboard. Default off for training.",
    },
  ];
  return (
    <section className="border-t border-ink/10 py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">Privacy</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Your inbox stays yours.
        </h2>
        <div className="mt-12 grid gap-6 sm:grid-cols-3">
          {items.map((i) => (
            <div
              key={i.title}
              className="rounded-2xl border border-ink/10 bg-white p-6"
            >
              <h3 className="text-base font-semibold">{i.title}</h3>
              <p className="mt-2 text-base leading-relaxed text-ink-soft">
                {i.body}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function Pricing() {
  return (
    <section id="pricing" className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">Pricing</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          One price.
          <br />
          <span className="text-ink-muted">Locked for founding members.</span>
        </h2>
        <div className="mt-12 flex justify-center">
          <div className="w-full max-w-md rounded-3xl border-2 border-ink bg-paper p-8">
            <p className="eyebrow text-accent">Founding member</p>
            <p className="mt-4 font-serif text-5xl tracking-tight">
              $9<span className="text-2xl text-ink-muted">/month</span>
            </p>
            <p className="mt-1 text-sm text-ink-muted">
              normally $19 — locked for life as long as you stay subscribed
            </p>
            <ul className="mt-6 space-y-2 text-base">
              <li>· Unlimited drafts</li>
              <li>· Google Calendar &amp; Outlook</li>
              <li>· 5-email voice training</li>
              <li>· Cancel anytime</li>
            </ul>
            <div className="mt-6">
              <WaitlistForm variant="secondary" />
              <p className="mt-3 text-xs text-ink-muted">
                No charge today. We&rsquo;ll email you when we launch in June 2026.
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

function FAQ() {
  const qs = [
    {
      q: "Is this just GPT with a calendar plugin?",
      a: "Yes and no. The calendar piece is two days of work. The hard part is making the reply sound like you, not like “Sure, happy to chat! Here are some times that work for me!” That’s where 80% of our engineering goes.",
    },
    {
      q: "Does it work in languages other than English?",
      a: "English at launch. Japanese support in v1.1 (Q4 2026).",
    },
    {
      q: "Will my prospect know it’s AI?",
      a: "Only if you tell them. There’s no link. No widget. No “powered by.” It’s an email you reviewed and sent.",
    },
    {
      q: "What if I want to schedule something internal?",
      a: "Use Calendly. We’re for the emails that matter.",
    },
    {
      q: "When does it launch?",
      a: "June 2026. Waitlist members get access two weeks early and the $9 price locked.",
    },
  ];
  return (
    <section id="faq" className="border-t border-ink/10 py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">FAQ</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Questions, mostly answered.
        </h2>
        <div className="mt-12 divide-y divide-ink/10 border-y border-ink/10">
          {qs.map((item) => (
            <details key={item.q} className="group py-6">
              <summary className="flex cursor-pointer list-none items-start justify-between gap-6 text-lg font-medium">
                {item.q}
                <span className="mt-1 text-ink-muted transition group-open:rotate-45">
                  +
                </span>
              </summary>
              <p className="mt-4 max-w-prose text-base leading-relaxed text-ink-soft">
                {item.a}
              </p>
            </details>
          ))}
        </div>
      </div>
    </section>
  );
}

function FinalCTA() {
  return (
    <section id="cta" className="border-t border-ink/10 bg-ink py-24 text-paper">
      <div className="container-prose text-center">
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Stop sending Calendly links
          <br />
          <span className="text-paper/60">to people who matter.</span>
        </h2>
        <p className="mt-6 text-lg text-paper/70">
          47 founding spots left at $9/mo. Launching June 2026.
        </p>
        <div className="mx-auto mt-10 max-w-xl">
          <WaitlistForm />
        </div>
      </div>
    </section>
  );
}

function Footer() {
  return (
    <footer className="container-prose flex flex-col items-start justify-between gap-4 py-12 text-sm text-ink-muted sm:flex-row">
      <p>&copy; {new Date().getFullYear()} Replyy. Built solo.</p>
      <div className="flex gap-6">
        <a href="mailto:hello@replyy.com" className="hover:text-ink">
          hello@replyy.com
        </a>
        <a href="#privacy" className="hover:text-ink">
          Privacy
        </a>
      </div>
    </footer>
  );
}
